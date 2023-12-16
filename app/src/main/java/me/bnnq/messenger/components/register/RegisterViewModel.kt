package me.bnnq.messenger.components.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import me.bnnq.messenger.services.abstractions.IAuthenticationService
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val authenticationService : IAuthenticationService) : ViewModel() {
    private val username = mutableStateOf("")
    private val password = mutableStateOf("")
    private val confirmPassword = mutableStateOf("")

    fun getUsername() = username.value
    fun getPassword() = password.value
    fun getConfirmPassword() = confirmPassword.value

    fun setUsername(newUsername: String) {
        viewModelScope.launch {
            username.value = newUsername
        }
    }

    fun setPassword(newPassword: String) {
        viewModelScope.launch {
            password.value = newPassword
        }
    }

    fun setConfirmPassword(newConfirmPassword: String) {
        viewModelScope.launch {
            confirmPassword.value = newConfirmPassword
        }
    }

    val registerErrorMessage: MutableStateFlow<String?> = MutableStateFlow(null)
    val registerResult: MutableStateFlow<Boolean?> = MutableStateFlow(null)
    var usernameValidationError by mutableStateOf<String?>(null)
    var passwordValidationError by mutableStateOf<String?>(null)
    var confirmationPasswordValidationError by mutableStateOf<String?>(null)

    fun register() {
        usernameValidationError = if(username.value.isEmpty()) "Username cannot be empty" else null
        passwordValidationError = if(password.value.isEmpty()) "Password cannot be empty" else null
        confirmationPasswordValidationError = if(confirmPassword.value.isEmpty() || confirmPassword.value != password.value) "Confirmation password cannot be empty" else null

        if (usernameValidationError != null || passwordValidationError != null || confirmationPasswordValidationError != null) {
            return
        }

        viewModelScope.launch {
            registerResult.value = authenticationService.register(username.value, password.value).success
        }
    }

}
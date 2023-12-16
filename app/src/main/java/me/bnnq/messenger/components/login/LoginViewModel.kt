package me.bnnq.messenger.components.login

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import me.bnnq.messenger.extensions.isEmptyOrWhitespace
import me.bnnq.messenger.models.Result
import me.bnnq.messenger.services.abstractions.IAuthenticationService
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val authenticationService : IAuthenticationService) : ViewModel() {
    private val username = mutableStateOf("")
    private val password = mutableStateOf("")

    fun getUsername() = username.value
    fun getPassword() = password.value

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

    val loginResult: MutableStateFlow<Result?> = MutableStateFlow(null)
    var usernameValidationError by mutableStateOf<String?>(null)
    var passwordValidationError by mutableStateOf<String?>(null)

    fun login() {
        usernameValidationError = if(username.value.isEmptyOrWhitespace()) "Username cannot be empty" else null
        passwordValidationError = if(password.value.isEmptyOrWhitespace()) "Password cannot be empty" else null

        if (usernameValidationError != null || passwordValidationError != null) {
            Log.d("LoginViewModel", "Validation error")
            return
        }

        viewModelScope.launch {
            loginResult.value = authenticationService.login(username.value, password.value)
        }
    }

}
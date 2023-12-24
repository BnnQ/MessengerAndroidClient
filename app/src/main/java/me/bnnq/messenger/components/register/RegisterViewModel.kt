package me.bnnq.messenger.components.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import me.bnnq.messenger.models.Result
import me.bnnq.messenger.models.eventargs.ServerUpdateEventArgs
import me.bnnq.messenger.models.eventargs.UserAuthenticationEventArgs
import me.bnnq.messenger.services.ServerCommunicationPool
import me.bnnq.messenger.services.abstractions.IAuthenticationService
import me.bnnq.messenger.services.abstractions.ViewModelBase
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val authenticationService: IAuthenticationService) :
    ViewModelBase()
{
    private val username = mutableStateOf("")
    private val password = mutableStateOf("")
    private val confirmPassword = mutableStateOf("")

    fun getUsername() =
        username.value

    fun getPassword() =
        password.value

    fun getConfirmPassword() =
        confirmPassword.value

    fun setUsername(newUsername: String)
    {
        viewModelScope.launch {
            username.value = newUsername
        }
    }

    fun setPassword(newPassword: String)
    {
        viewModelScope.launch {
            password.value = newPassword
        }
    }

    fun setConfirmPassword(newConfirmPassword: String)
    {
        viewModelScope.launch {
            confirmPassword.value = newConfirmPassword
        }
    }

    var usernameValidationError by mutableStateOf<String?>(null)
    var passwordValidationError by mutableStateOf<String?>(null)
    var confirmationPasswordValidationError by mutableStateOf<String?>(null)
    val registerResult: MutableSharedFlow<Result?> = MutableSharedFlow(replay = 0)

    fun register()
    {
        usernameValidationError = if (username.value.isEmpty()) "Username cannot be empty" else null
        passwordValidationError = if (password.value.isEmpty()) "Password cannot be empty" else null
        confirmationPasswordValidationError =
            if (confirmPassword.value.isEmpty() || confirmPassword.value != password.value) "Confirmation password cannot be empty" else null

        if (usernameValidationError != null || passwordValidationError != null || confirmationPasswordValidationError != null)
        {
            return
        }

        viewModelScope.launch {
            authenticationService.register(username.value, password.value)
        }
    }

    private val onUserRegistered = fun(_: ServerUpdateEventArgs)
    {
        viewModelScope.launch {
            authenticationService.login(username.value, password.value)
        }
    }

    private val onUserAuthenticated = fun(_: UserAuthenticationEventArgs)
    {
        viewModelScope.launch {
            registerResult.emit(Result())
        }
    }

    override fun onInit()
    {
        ServerCommunicationPool.actionUpdateEvent += onUserRegistered
        ServerCommunicationPool.userAuthenticatedEvent += onUserAuthenticated
    }

    override fun onDispose()
    {
        ServerCommunicationPool.actionUpdateEvent -= onUserRegistered
        ServerCommunicationPool.userAuthenticatedEvent -= onUserAuthenticated
    }

}
package me.bnnq.messenger.components.login

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

import me.bnnq.messenger.models.Result
import me.bnnq.messenger.models.eventargs.UserAuthenticationEventArgs
import me.bnnq.messenger.services.ServerCommunicationPool
import me.bnnq.messenger.services.abstractions.IAuthenticationService
import me.bnnq.messenger.services.abstractions.ViewModelBase
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authenticationService: IAuthenticationService) :
    ViewModelBase()
{
    private val username = mutableStateOf("")
    private val password = mutableStateOf("")

    fun getUsername() =
        username.value

    fun getPassword() =
        password.value

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

    var usernameValidationError by mutableStateOf<String?>(null)
    var passwordValidationError by mutableStateOf<String?>(null)

    val loginResult: MutableSharedFlow<Result?> = MutableSharedFlow(replay = 0)
    fun login()
    {
        usernameValidationError = if (username.value.isBlank()) "Username cannot be empty" else null
        passwordValidationError = if (password.value.isBlank()) "Password cannot be empty" else null

        if (usernameValidationError != null || passwordValidationError != null)
        {
            Log.d("LoginViewModel", "Validation error")
            return
        }

        viewModelScope.launch {
            authenticationService.login(username.value, password.value)
        }
    }

    private val onUserAuthenticated = fun(_: UserAuthenticationEventArgs)
    {
        viewModelScope.launch {
            loginResult.emit(Result())
        }
    }

    override fun onInit()
    {
        ServerCommunicationPool.userAuthenticatedEvent += onUserAuthenticated
    }

    override fun onDispose()
    {
        ServerCommunicationPool.userAuthenticatedEvent -= onUserAuthenticated
    }
}
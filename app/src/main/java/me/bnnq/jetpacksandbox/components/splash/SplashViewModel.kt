package me.bnnq.jetpacksandbox.components.splash

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import me.bnnq.jetpacksandbox.services.abstractions.IAuthenticationService
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(val authenticationService: IAuthenticationService) : ViewModel() {
}
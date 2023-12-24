package me.bnnq.messenger.components.splash

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import me.bnnq.messenger.services.ServerCommunicationPool
import me.bnnq.messenger.services.abstractions.IAuthenticationService
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    val authenticationService: IAuthenticationService) : ViewModel()
{
}
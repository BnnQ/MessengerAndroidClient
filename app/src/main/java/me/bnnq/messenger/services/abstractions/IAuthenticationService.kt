package me.bnnq.messenger.services.abstractions

import me.bnnq.messenger.models.Result
import me.bnnq.messenger.models.User

interface IAuthenticationService {
    fun login(username: String, password: String, trackAuthenticationTime : Boolean = true): Result
    fun register(username: String, password: String): Result
    fun getCurrentUser(): User?
    fun isAuthenticated(): Boolean
}
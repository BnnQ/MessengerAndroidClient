package me.bnnq.jetpacksandbox.services.abstractions

import me.bnnq.jetpacksandbox.models.Result
import me.bnnq.jetpacksandbox.models.User

interface IAuthenticationService {
    fun login(username: String, password: String, trackAuthenticationTime : Boolean = true): Result
    fun register(username: String, password: String): Result
    fun getCurrentUser(): User?
    fun isAuthenticated(): Boolean
}
package me.bnnq.jetpacksandbox.services

import me.bnnq.jetpacksandbox.models.Result
import me.bnnq.jetpacksandbox.models.User
import me.bnnq.jetpacksandbox.services.abstractions.IAuthenticationService

class StubAuthenticationService : IAuthenticationService {
    private var isAuthenticated = false
    override fun login(
        username: String,
        password: String,
        trackAuthenticationTime: Boolean
    ): Result {
        isAuthenticated = true
        return Result()
    }

    override fun register(username: String, password: String): Result {
        isAuthenticated = true
        return Result()
    }

    override fun getCurrentUser(): User? {
        return User(1, "test", "test")
    }

    override fun isAuthenticated(): Boolean {
        return isAuthenticated
    }

}
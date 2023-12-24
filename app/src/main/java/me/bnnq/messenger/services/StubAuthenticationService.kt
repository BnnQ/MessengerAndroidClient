package me.bnnq.messenger.services

import me.bnnq.messenger.models.Result
import me.bnnq.messenger.models.User
import me.bnnq.messenger.services.abstractions.IAuthenticationService

//class StubAuthenticationService : IAuthenticationService
//{
//    private var isAuthenticated = false
//    override fun login(
//        username: String,
//        password: String,
//        trackAuthenticationTime: Boolean
//    ): Result
//    {
//        isAuthenticated = true
//        return Result()
//    }
//
//    override fun register(username: String, password: String): Result
//    {
//        isAuthenticated = true
//        return Result()
//    }
//
////    override fun getCurrentUser(): User?
////    {
////        return User(1, "test", "test")
////    }
//
//}
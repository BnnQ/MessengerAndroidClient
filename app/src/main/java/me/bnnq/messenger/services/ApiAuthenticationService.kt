package me.bnnq.messenger.services

import me.bnnq.messenger.utils.ServerRequestBuilder
import android.util.Log
import com.android.volley.Request
import me.bnnq.messenger.BuildConfig
import me.bnnq.messenger.models.Result
import me.bnnq.messenger.models.User
import me.bnnq.messenger.services.abstractions.IAuthenticationService

class ApiAuthenticationService : IAuthenticationService
{
    override fun login(username: String, password: String, trackAuthenticationTime: Boolean): String?
    {
        val body = object
        {
            val username = username
            val password = password
            val fcmToken = ServerCommunicationPool.lastFcmToken
        }

        val request = ServerRequestBuilder()
            .setUrl("${BuildConfig.API_URL}/auth/login")
            .setBody(body)
            .setMethod(Request.Method.POST)
            .withActionIdentifier()
            .builder

        return ServerCommunicationPool.pushRequest(request)
    }

    override fun register(username: String, password: String): String?
    {
        val body = object
        {
            val username = username
            val password = password
            val fcmToken = ServerCommunicationPool.lastFcmToken
        }

        val request = ServerRequestBuilder()
            .setUrl("${BuildConfig.API_URL}/auth/register")
            .setBody(body)
            .setMethod(Request.Method.POST)
            .withActionIdentifier()
            .builder

        return ServerCommunicationPool.pushRequest(request)
    }
}
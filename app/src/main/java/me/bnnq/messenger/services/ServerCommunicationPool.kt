package me.bnnq.messenger.services

import me.bnnq.messenger.utils.ServerRequestBuilder
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import kotlinx.coroutines.runBlocking
import me.bnnq.messenger.BuildConfig
import me.bnnq.messenger.MessengerApplication
import me.bnnq.messenger.extensions.uniqueActionIdentifier
import me.bnnq.messenger.models.User
import me.bnnq.messenger.models.dto.UserInfoDto
import me.bnnq.messenger.models.eventargs.ServerUpdateEventArgs
import me.bnnq.messenger.models.eventargs.UserAuthenticationEventArgs
import me.bnnq.messenger.utils.Event
import java.util.LinkedList
import java.util.Queue
import java.util.UUID
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class ServerCommunicationPool
{
    companion object
    {
        val actionUpdateEvent = Event<ServerUpdateEventArgs>()
        val userAuthenticatedEvent = Event<UserAuthenticationEventArgs>()
        var lastFcmToken: String? = null
        var jwtToken: String? = null
        var currentUser: UserInfoDto? = null
        private val requestQueue: RequestQueue by lazy {
            Volley.newRequestQueue(MessengerApplication.instance.applicationContext)
        }
        private val authorizedRequestQueue: Queue<() -> Request<*>> = LinkedList()

        fun getNewActionIdentifier(): String =
            UUID.randomUUID().uniqueActionIdentifier(currentUser?.id)

        fun getAuthenticationHeader(): String
        {
            return "Bearer $jwtToken"
        }

        private suspend fun isUserAuthenticated(): Boolean =
            suspendCoroutine { continuation ->
                if (jwtToken == null || currentUser == null)
                {
                    continuation.resume(false)
                }
                else
                {
                    continuation.resume(true)
//                    val request = ServerRequestBuilder()
//                        .setUrl("${BuildConfig.API_URL}/auth/IsAuthenticated")
//                        .setMethod(Request.Method.GET)
//                        .withCredentials()
//                        .withActionIdentifier()
//                        .setStringResponseListener { _ ->
//                            continuation.resume(true)
//                        }
//                        .setErrorListener { _ ->
//                            continuation.resume(false)
//                        }
//                        .builder
//
//                    requestQueue.add(request())
                }
            }

        fun pushRequest(requestBuilder: () -> Request<*>): String?
        {
            val request = requestBuilder()
            if (request.headers["Authorization"] != null && !isCurrentUserAuthenticated())
                authorizedRequestQueue.add(requestBuilder)
            else
                requestQueue.add(request)

            return request.headers["ActionIdentifier"]
        }

        fun pushAuthorizedRequests()
        {
            while (authorizedRequestQueue.isNotEmpty())
            {
                val requestBuilder = authorizedRequestQueue.poll()
                val request = requestBuilder?.let { it() }
                requestQueue.add(request)
            }
        }

        fun updateFcmTokenOnServer(token: String)
        {
            lastFcmToken = token

            val body = object
            {
                val fcmToken = token
            }

            val request = ServerRequestBuilder()
                .setUrl("${BuildConfig.API_URL}/client/refreshFcmToken")
                .setMethod(Request.Method.POST)
                .setBody(body)
                .setJsonResponseListener { response ->
                    Log.d("ServerCommunicationPool", "Token updated on server: $response")
                }
                .setErrorListener { error ->
                    Log.d("ServerCommunicationPool", "Error while updating token on server: $error")
                }
                .withCredentials()
                .builder

            pushRequest(request)
        }

        fun isCurrentUserAuthenticated(): Boolean
        {
            val result = runBlocking {
                return@runBlocking isUserAuthenticated()
            }

            return result
        }
    }
}
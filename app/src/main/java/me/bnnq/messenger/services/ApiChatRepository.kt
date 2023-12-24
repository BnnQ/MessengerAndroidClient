package me.bnnq.messenger.services

import com.android.volley.Request
import me.bnnq.messenger.BuildConfig
import me.bnnq.messenger.models.Chat
import me.bnnq.messenger.services.abstractions.IChatRepository
import me.bnnq.messenger.utils.ServerRequestBuilder

class ApiChatRepository : IChatRepository
{
    override fun getUserChats(userId: String): String?
    {
        val request = ServerRequestBuilder()
            .setUrl("${BuildConfig.API_URL}/chat/GetChatsByUserId/$userId")
            .setMethod(Request.Method.GET)
            .withCredentials()
            .withActionIdentifier()
            .builder

        return ServerCommunicationPool.pushRequest(request)
    }

    override fun getChatUsers(chatId: Int): String?
    {
        val request = ServerRequestBuilder()
            .setUrl("${BuildConfig.API_URL}/chat/GetChatUsers/$chatId")
            .setMethod(Request.Method.GET)
            .withCredentials()
            .withActionIdentifier()
            .builder

        return ServerCommunicationPool.pushRequest(request)
    }

    override fun createChat(creatorId: String, userUsernames: List<String>): String?
    {
        val body = object
        {
            val creatorId = creatorId
            val userUsernames = userUsernames
        }

        val request = ServerRequestBuilder()
            .setUrl("${BuildConfig.API_URL}/chat/CreateChat")
            .setMethod(Request.Method.POST)
            .setBody(body)
            .withCredentials()
            .withActionIdentifier()
            .builder

        return ServerCommunicationPool.pushRequest(request)
    }
}
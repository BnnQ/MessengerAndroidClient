package me.bnnq.messenger.services

import com.android.volley.Request
import me.bnnq.messenger.BuildConfig
import me.bnnq.messenger.models.Message
import me.bnnq.messenger.services.abstractions.IMessageRepository
import me.bnnq.messenger.utils.ServerRequestBuilder
import java.util.Date

class ApiMessageRepository : IMessageRepository
{
    override fun getChatMessages(chatId: Int): String?
    {
        val request = ServerRequestBuilder()
            .setUrl("${BuildConfig.API_URL}/message/GetMessagesByChatId/$chatId")
            .setMethod(Request.Method.GET)
            .withCredentials()
            .withActionIdentifier()
            .builder

        return ServerCommunicationPool.pushRequest(request)
    }

    override fun sendMessage(chatId: Int, message: Message): String?
    {
        val request = ServerRequestBuilder()
            .setUrl("${BuildConfig.API_URL}/message/SendMessage")
            .setMethod(Request.Method.POST)
            .setBody(message)
            .withCredentials()
            .withActionIdentifier()
            .builder

        return ServerCommunicationPool.pushRequest(request)
    }
}
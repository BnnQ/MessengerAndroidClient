package me.bnnq.jetpacksandbox.services

import me.bnnq.jetpacksandbox.models.Chat
import me.bnnq.jetpacksandbox.services.abstractions.IChatRepository

class StubChatRepository : IChatRepository {
    private val chats : List<Chat> = arrayListOf(
        Chat(1, 1, 2)
    )

    override fun getUserChats(userId: Int): List<Chat> {
        return chats.filter { it.firstUserId == userId || it.secondUserId == userId }
    }
}
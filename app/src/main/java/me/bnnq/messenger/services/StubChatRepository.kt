package me.bnnq.messenger.services

import me.bnnq.messenger.models.Chat
import me.bnnq.messenger.services.abstractions.IChatRepository

class StubChatRepository : IChatRepository {
    private val chats : List<Chat> = arrayListOf(
        Chat(1, 1, 2)
    )

    override fun getUserChats(userId: Int): List<Chat> {
        return chats.filter { it.firstUserId == userId || it.secondUserId == userId }
    }
}
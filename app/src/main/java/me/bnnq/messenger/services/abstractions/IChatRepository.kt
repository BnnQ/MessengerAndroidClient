package me.bnnq.messenger.services.abstractions

import me.bnnq.messenger.models.Chat

interface IChatRepository
{
    fun getUserChats(userId: String): String?
    fun getChatUsers(chatId: Int): String?
    fun createChat(creatorId: String, userUsernames: List<String>): String?
}
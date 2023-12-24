package me.bnnq.messenger.services.abstractions

import me.bnnq.messenger.models.Message

interface IMessageRepository
{
    fun getChatMessages(chatId: Int): String?
    fun sendMessage(chatId: Int, message: Message): String?
}
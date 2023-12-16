package me.bnnq.jetpacksandbox.services.abstractions

import me.bnnq.jetpacksandbox.models.Chat
import me.bnnq.jetpacksandbox.models.Message

interface IMessageRepository
{
    fun getChatMessages(chatId: Int): List<Message>;
    fun sendMessage(chatId: Int, message: Message): Message?;
}
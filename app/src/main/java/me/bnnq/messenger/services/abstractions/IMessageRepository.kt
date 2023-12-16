package me.bnnq.messenger.services.abstractions

import me.bnnq.messenger.models.Message

interface IMessageRepository
{
    fun getChatMessages(chatId: Int): List<Message>;
    fun sendMessage(chatId: Int, message: Message): Message?;
}
package me.bnnq.messenger.services.abstractions

import me.bnnq.messenger.models.Chat

interface IChatRepository {
    fun getUserChats(userId : Int) : List<Chat>;
}
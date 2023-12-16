package me.bnnq.jetpacksandbox.services.abstractions

import me.bnnq.jetpacksandbox.models.Chat

interface IChatRepository {
    fun getUserChats(userId : Int) : List<Chat>;
}
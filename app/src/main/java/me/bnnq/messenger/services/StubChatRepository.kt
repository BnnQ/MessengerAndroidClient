package me.bnnq.messenger.services

import me.bnnq.messenger.models.Chat
import me.bnnq.messenger.models.User
import me.bnnq.messenger.services.abstractions.IChatRepository

//class StubChatRepository : IChatRepository
//{
//    private val chats: List<Chat> = arrayListOf(
//        Chat(1, listOf("228a41a1-816c-4f6b-b64d-d66717e08853", "0fea675f-dd69-40fe-90db-ba23f1f354ce"))
//    )
//
//    override fun getUserChats(userId: String): List<Chat>
//    {
//        return chats.filter {
//            it.userIds.any { id -> id == userId }
//        }
//    }
//}
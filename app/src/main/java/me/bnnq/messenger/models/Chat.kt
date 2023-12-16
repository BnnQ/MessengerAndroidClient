package me.bnnq.messenger.models

data class Chat(
    val id: Int,
    val firstUserId: Int,
    val secondUserId: Int
)
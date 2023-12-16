package me.bnnq.messenger.models

import java.util.Date

data class Message(
    val chatId : Int,
    val senderId : Int,
    val content : String,
    val sentAt : Date
)
package me.bnnq.jetpacksandbox.models

import java.util.Date

data class Message(
    val chatId : Int,
    val senderId : Int,
    val content : String,
    val sentAt : Date
)
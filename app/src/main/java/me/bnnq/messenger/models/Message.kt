package me.bnnq.messenger.models

import com.google.gson.annotations.SerializedName
import java.util.Date

data class Message(
    @SerializedName("ChatId")
    val chatId: Int,
    @SerializedName("SenderId")
    val senderId: String,
    @SerializedName("Text")
    val text: String,
    @SerializedName("SentAt")
    val sentAt: Date
)
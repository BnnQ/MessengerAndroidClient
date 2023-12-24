package me.bnnq.messenger.models.dto

import com.google.gson.annotations.SerializedName
import me.bnnq.messenger.models.Message

data class ChatInfoDto(
    @SerializedName("Id")
    val id: Int,
    @SerializedName("Users")
    val users: List<UserInfoDto>,
    @SerializedName("LastMessage")
    var lastMessage: Message?,
)
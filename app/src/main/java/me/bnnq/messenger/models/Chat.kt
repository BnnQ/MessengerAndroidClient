package me.bnnq.messenger.models

import com.google.gson.annotations.SerializedName

data class Chat(
    @SerializedName("Id")
    val id: Int,
    val userIds: List<String>
)
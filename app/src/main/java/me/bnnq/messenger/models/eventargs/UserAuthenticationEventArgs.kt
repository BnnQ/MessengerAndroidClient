package me.bnnq.messenger.models.eventargs

import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName
import me.bnnq.messenger.enums.ActionType

data class UserAuthenticationEventArgs(
    val currentUserId: String,
    val token: String
)
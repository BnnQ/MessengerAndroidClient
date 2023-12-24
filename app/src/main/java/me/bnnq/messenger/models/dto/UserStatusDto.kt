package me.bnnq.messenger.models.dto

import com.google.gson.annotations.SerializedName
import me.bnnq.messenger.enums.StatusType

data class UserStatusDto(
    @SerializedName("UserId")
    val userId: String,
    @SerializedName("Status")
    val status: StatusType
)

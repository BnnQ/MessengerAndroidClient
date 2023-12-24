package me.bnnq.messenger.models.dto

import com.google.gson.annotations.SerializedName

data class UserInfoDto(
    @SerializedName("Id")
    val id: String,
    @SerializedName("UserName")
    val username: String,
    @SerializedName("AvatarImagePath")
    val avatarImagePath: String,
)
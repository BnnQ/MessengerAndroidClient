package me.bnnq.messenger.models

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    @SerializedName("Id")
    val id: String,
    @SerializedName("UserName")
    val username: String,
    @SerializedName("Password")
    val password: String)
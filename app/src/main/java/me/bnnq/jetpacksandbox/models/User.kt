package me.bnnq.jetpacksandbox.models

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id : Int,
    val username: String,
    val password: String)
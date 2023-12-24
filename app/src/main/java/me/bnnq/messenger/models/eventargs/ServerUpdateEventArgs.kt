package me.bnnq.messenger.models.eventargs

import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import me.bnnq.messenger.enums.ActionType
import org.json.JSONObject

data class ServerUpdateEventArgs(
    @SerializedName("ActionId")
    val actionId: String,
    @SerializedName("ActionType")
    val actionType: ActionType,
    @SerializedName("IsSuccess")
    val isSuccess: Boolean,
    @SerializedName("ActionData")
    val actionData: JsonElement? = null
)
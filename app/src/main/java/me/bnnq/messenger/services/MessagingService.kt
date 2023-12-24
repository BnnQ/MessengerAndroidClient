package me.bnnq.messenger.services

import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.google.gson.JsonParser
import me.bnnq.messenger.enums.ActionType
import me.bnnq.messenger.extensions.fromJson
import me.bnnq.messenger.models.User
import me.bnnq.messenger.models.dto.UserInfoDto
import me.bnnq.messenger.models.eventargs.ServerUpdateEventArgs
import me.bnnq.messenger.models.eventargs.UserAuthenticationEventArgs

class MessagingService : FirebaseMessagingService()
{
    private val onActionUpdateEvent = fun(eventArgs: ServerUpdateEventArgs)
    {
        if (eventArgs.actionType == ActionType.UserLoggedIn)
        {
            val token = eventArgs.actionData!!.asJsonObject.get("Token").asString
            val currentUser =
                Gson().fromJson<UserInfoDto>(eventArgs.actionData.asJsonObject.get("CurrentUser").toString())
            ServerCommunicationPool.jwtToken = token
            ServerCommunicationPool.currentUser = currentUser

            ServerCommunicationPool.userAuthenticatedEvent(UserAuthenticationEventArgs(currentUser.id, token))

            ServerCommunicationPool.pushAuthorizedRequests()
        }
    }

    private val onUserUpdatedEvent = fun(eventArgs: ServerUpdateEventArgs)
    {
        if (eventArgs.actionType == ActionType.UserUpdated)
        {
            val updatedUser = Gson().fromJson<UserInfoDto>(eventArgs.actionData.toString())
            ServerCommunicationPool.currentUser = updatedUser
        }
    }

    override fun onCreate()
    {
        super.onCreate()

        ServerCommunicationPool.actionUpdateEvent += onActionUpdateEvent
        ServerCommunicationPool.actionUpdateEvent += onUserUpdatedEvent
    }

    override fun onDestroy()
    {
        super.onDestroy()

        ServerCommunicationPool.actionUpdateEvent -= onActionUpdateEvent
        ServerCommunicationPool.actionUpdateEvent -= onUserUpdatedEvent
    }

    override fun onMessageReceived(message: RemoteMessage)
    {
        val dataMap = message.data
        if (dataMap.isNotEmpty())
        {
            val actionDataString = dataMap["ActionData"]
            val actionData = if (actionDataString != null) JsonParser.parseString(actionDataString) else null

            val eventArgs = ServerUpdateEventArgs(
                actionId = dataMap["ActionId"] ?: "",
                actionType = ActionType.valueOf(dataMap["ActionType"] ?: ""),
                isSuccess = dataMap["IsSuccess"]?.toBoolean() ?: false,
                actionData = actionData
            )

            ServerCommunicationPool.actionUpdateEvent(eventArgs)
        }

        super.onMessageReceived(message)
    }

    override fun onNewToken(token: String)
    {
        super.onNewToken(token)

        ServerCommunicationPool.updateFcmTokenOnServer(token)
    }
}
package me.bnnq.messenger.components.conversations

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import me.bnnq.messenger.BuildConfig
import me.bnnq.messenger.enums.ActionType
import me.bnnq.messenger.extensions.fromJson
import me.bnnq.messenger.models.Chat
import me.bnnq.messenger.models.Message
import me.bnnq.messenger.models.dto.ChatInfoDto
import me.bnnq.messenger.models.dto.UserInfoDto
import me.bnnq.messenger.models.eventargs.ServerUpdateEventArgs
import me.bnnq.messenger.models.eventargs.UserAuthenticationEventArgs
import me.bnnq.messenger.services.ServerCommunicationPool
import me.bnnq.messenger.services.abstractions.IAuthenticationService
import me.bnnq.messenger.services.abstractions.IChatRepository
import me.bnnq.messenger.services.abstractions.ViewModelBase
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType
import okhttp3.MediaType.Companion.parse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class ConversationsViewModel @Inject constructor(
    private val authenticationService: IAuthenticationService,
    private val chatRepository: IChatRepository,
    @ApplicationContext private val context: Context,
) : ViewModelBase()
{
    private val _chats = MutableStateFlow<List<ChatInfoDto>>(emptyList())
    val chats: StateFlow<List<ChatInfoDto>> = _chats

    // Add this to keep track of the chat with the new message
    val newMessageChat = MutableStateFlow<ChatInfoDto?>(null)

    private val onChatListUpdated = fun(eventArgs: ServerUpdateEventArgs)
    {
        if (eventArgs.actionType == ActionType.ChatListUpdated)
        {
            val gson = GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
                .create()

            _chats.value = gson.fromJson<List<ChatInfoDto>>(eventArgs.actionData?.toString() ?: "[]")
        }
    }

    private val onUserAuthenticated = fun(eventArgs: UserAuthenticationEventArgs)
    {
        chatRepository.getUserChats(eventArgs.currentUserId)
    }

    private val onChatCreated = fun(eventArgs: ServerUpdateEventArgs)
    {
        if (eventArgs.actionType == ActionType.ChatCreated)
        {
            val gson = GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
                .create()

            val chat = gson.fromJson<ChatInfoDto>(eventArgs.actionData?.toString() ?: "{}")
            _chats.value = _chats.value + chat
        }
    }

    // Add this to handle new messages
    private val onMessageReceived = fun(eventArgs: ServerUpdateEventArgs)
    {
        if (eventArgs.actionType == ActionType.MessageReceived)
        {
            val gson = GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
                .create()

            val message = gson.fromJson<Message>(eventArgs.actionData?.toString() ?: "{}")
            val chat = _chats.value.find { it.id == message.chatId }
            chat?.lastMessage = message
            newMessageChat.value = chat
        }
    }

    private val _avatarUrl = MutableLiveData(ServerCommunicationPool.currentUser?.avatarImagePath)
    val avatarUrl: LiveData<String?> get() = _avatarUrl

    private val onUserUpdated = fun(eventArgs: ServerUpdateEventArgs)
    {
        if (eventArgs.actionType == ActionType.UserUpdated)
        {
            val gson = GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
                .create()

            val updatedUser = gson.fromJson<UserInfoDto>(eventArgs.actionData?.toString() ?: "{}")
            viewModelScope.launch {
                _avatarUrl.value = updatedUser.avatarImagePath
            }

        }
    }

    // Add this function to handle the dialog
    fun createChat(newChatUserUsername: String)
    {
        val userUsernames = listOf(newChatUserUsername)
        val creatorId = ServerCommunicationPool.currentUser!!.id

        chatRepository.createChat(creatorId, userUsernames)
    }

    fun updateAvatar(uri: Uri)
    {
        val contentResolver = context.contentResolver
        val inputStream = contentResolver.openInputStream(uri)
        val bytes = inputStream?.use { it.readBytes() } // This automatically closes the InputStream
        val contentType = contentResolver.getType(uri)?.toMediaTypeOrNull()
        val requestBody = bytes?.toRequestBody(contentType)
            ?: // Handle the error here
            return

        val multipartBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("avatar", "image.jpg", requestBody)
            .build()

        val request = Request.Builder()
            .url("${BuildConfig.API_URL}/user/UpdateUser")
            .post(multipartBody)
            .addHeader("Authorization", ServerCommunicationPool.getAuthenticationHeader())
            .addHeader("ActionIdentifier", ServerCommunicationPool.getNewActionIdentifier())
            .build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback
        {
            override fun onFailure(call: Call, e: IOException)
            {
                Log.d("Avatar", "Avatar update failed")
            }

            override fun onResponse(call: Call, response: Response)
            {
                Log.d("Avatar", "Avatar updated")
            }
        })

    }

    override fun onInit()
    {
        ServerCommunicationPool.actionUpdateEvent += onChatListUpdated
        ServerCommunicationPool.actionUpdateEvent += onChatCreated
        ServerCommunicationPool.actionUpdateEvent += onMessageReceived
        ServerCommunicationPool.actionUpdateEvent += onUserUpdated
        ServerCommunicationPool.userAuthenticatedEvent.subscribeOnceImmediately(onUserAuthenticated)
    }

    override fun onDispose()
    {
        ServerCommunicationPool.actionUpdateEvent -= onChatListUpdated
        ServerCommunicationPool.actionUpdateEvent -= onChatCreated
        ServerCommunicationPool.actionUpdateEvent -= onMessageReceived
        ServerCommunicationPool.actionUpdateEvent -= onUserUpdated
        ServerCommunicationPool.userAuthenticatedEvent -= onUserAuthenticated
    }

}
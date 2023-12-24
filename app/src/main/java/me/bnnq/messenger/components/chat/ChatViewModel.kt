package me.bnnq.messenger.components.chat

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.google.gson.GsonBuilder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import me.bnnq.messenger.enums.ActionType
import me.bnnq.messenger.extensions.fromJson
import me.bnnq.messenger.models.Message
import me.bnnq.messenger.models.dto.UserInfoDto
import me.bnnq.messenger.models.dto.UserStatusDto
import me.bnnq.messenger.models.eventargs.ServerUpdateEventArgs
import me.bnnq.messenger.services.ServerCommunicationPool
import me.bnnq.messenger.services.abstractions.IChatRepository
import me.bnnq.messenger.services.abstractions.IMessageRepository
import me.bnnq.messenger.services.abstractions.ViewModelBase
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val messageRepository: IMessageRepository,
    private val chatRepository: IChatRepository,
    savedStateHandle: SavedStateHandle
) : ViewModelBase()
{
    val chatName = mutableStateOf("")
    val chatAvatar = mutableStateOf("")
    val otherUserId = mutableStateOf<String?>(null)

    private val onChatUpdated = fun(eventArgs: ServerUpdateEventArgs)
    {
        if (eventArgs.actionType == ActionType.ChatMessagesUpdated)
        {
            val gson = GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
                .create()
            _messages.value = gson.fromJson<List<Message>>(eventArgs.actionData!!.toString())
        }
    }

    private val onMessageSent = fun(eventArgs: ServerUpdateEventArgs)
    {
        if (eventArgs.actionType == ActionType.MessageSent)
        {
            val gson = GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
                .create()
            _messages.value = _messages.value + gson.fromJson<Message>(eventArgs.actionData!!.toString())
        }
    }

    private val onMessageReceived = fun(eventArgs: ServerUpdateEventArgs)
    {
        if (eventArgs.actionType == ActionType.MessageReceived)
        {
            val gson = GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
                .create()
            _messages.value = _messages.value + gson.fromJson<Message>(eventArgs.actionData!!.toString())
        }
    }

    private val onChatUsersUpdated = fun(eventArgs: ServerUpdateEventArgs)
    {
        if (eventArgs.actionType == ActionType.ChatUsersUpdated)
        {
            val gson = GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
                .create()

            val chatUsers =
                gson.fromJson<List<UserInfoDto>>(eventArgs.actionData!!.toString())
                    .filter { it.id != ServerCommunicationPool.currentUser!!.id }
            val otherUser = chatUsers.first()
            chatName.value = otherUser.username
            chatAvatar.value = otherUser.avatarImagePath
            otherUserId.value = otherUser.id
        }
    }

    val isUserOnline = MutableStateFlow(false)
    private var delayJob: Job? = null
    private val onOtherUserStatusChanged = fun(eventArgs: ServerUpdateEventArgs)
    {
        if (eventArgs.actionType == ActionType.UserStatusChanged)
        {
            val gson = GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
                .create()

            val userStatus = gson.fromJson<UserStatusDto>(eventArgs.actionData!!.toString())
            if (userStatus.userId == otherUserId.value)
            {
                delayJob?.cancel()
                isUserOnline.value = true

                delayJob = viewModelScope.launch {
                    delay(15000) // wait for 30 seconds
                    isUserOnline.value = false
                }
            }
        }
    }

    override fun onInit()
    {
        ServerCommunicationPool.actionUpdateEvent += onChatUpdated
        ServerCommunicationPool.actionUpdateEvent += onMessageReceived
        ServerCommunicationPool.actionUpdateEvent += onOtherUserStatusChanged

        loadMessages()
        loadChatUsers()
    }

    override fun onDispose()
    {
        ServerCommunicationPool.actionUpdateEvent -= onChatUpdated
        ServerCommunicationPool.actionUpdateEvent -= onMessageReceived
        ServerCommunicationPool.actionUpdateEvent -= onOtherUserStatusChanged
        delayJob?.cancel()
    }

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages

    val chatId = savedStateHandle.get<String>("chatId")?.toIntOrNull()

    fun loadMessages()
    {
        if (chatId == null)
            return

        viewModelScope.launch {
            messageRepository.getChatMessages(chatId)
        }
    }

    fun loadChatUsers()
    {
        if (chatId == null)
            return

        viewModelScope.launch {
            val actionId = chatRepository.getChatUsers(chatId)
            ServerCommunicationPool.actionUpdateEvent.subscribeOnceWithCondition(onChatUsersUpdated) { it.actionId == actionId }
        }
    }

    private val message = mutableStateOf("")

    fun getMessage() =
        message.value

    fun setMessage(newMessage: String)
    {
        viewModelScope.launch {
            message.value = newMessage
        }
    }

    fun sendMessage()
    {
        if (ServerCommunicationPool.currentUser?.id != null)
        {
            viewModelScope.launch {
                val actionId = messageRepository.sendMessage(
                    chatId!!,
                    Message(
                        chatId,
                        ServerCommunicationPool.currentUser!!.id,
                        message.value,
                        Date())
                )

                setMessage("")

                ServerCommunicationPool.actionUpdateEvent.subscribeOnceWithCondition(onMessageSent) { it.actionId == actionId }
            }
        }
    }

}

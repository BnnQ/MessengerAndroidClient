package me.bnnq.jetpacksandbox.components.chat

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import me.bnnq.jetpacksandbox.models.Message
import me.bnnq.jetpacksandbox.models.Result
import me.bnnq.jetpacksandbox.services.abstractions.IAuthenticationService
import me.bnnq.jetpacksandbox.services.abstractions.IMessageRepository
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val messageRepository: IMessageRepository,
    private val authenticationService: IAuthenticationService,
    savedStateHandle: SavedStateHandle
) : ViewModel()
{
    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages

    val chatId = savedStateHandle.get<String>("chatId")?.toIntOrNull()
    val currentUserId = authenticationService.getCurrentUser()?.id

    fun loadMessages()
    {
        if (chatId == null)
            return

        viewModelScope.launch {
            _messages.value = messageRepository.getChatMessages(chatId)
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

    val sendMessageResult: MutableStateFlow<Result?> = MutableStateFlow(null)

    fun sendMessage()
    {
        if (currentUserId != null)
        {
            viewModelScope.launch {
                val sentMessage = messageRepository.sendMessage(
                    chatId!!,
                    Message(
                        chatId,
                        currentUserId,
                        message.value,
                        Date())
                )

                if (sentMessage != null)
                {
                    _messages.value = _messages.value + sentMessage
                    message.value = ""
                    sendMessageResult.value = Result()
                } else
                {
                    sendMessageResult.value = Result("Failed to send message")
                }
            }
        }
    }

}

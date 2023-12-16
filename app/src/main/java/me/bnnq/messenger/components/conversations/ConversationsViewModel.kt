package me.bnnq.messenger.components.conversations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import me.bnnq.messenger.models.Chat
import me.bnnq.messenger.services.abstractions.IAuthenticationService
import me.bnnq.messenger.services.abstractions.IChatRepository
import javax.inject.Inject

@HiltViewModel
class ConversationsViewModel @Inject constructor(
    private val authenticationService: IAuthenticationService,
    private val chatRepository: IChatRepository
) : ViewModel()
{
    private val _chats = MutableStateFlow<List<Chat>>(emptyList())
    val chats: StateFlow<List<Chat>> = _chats

    init
    {
        if (authenticationService.isAuthenticated())
        {
            viewModelScope.launch {
                val currentUserId = authenticationService.getCurrentUser()!!.id
                _chats.value = chatRepository.getUserChats(currentUserId)
            }
        }
    }
}
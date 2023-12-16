package me.bnnq.messenger.components.chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import me.bnnq.messenger.models.Message
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun ChatPage(
    viewModel: ChatViewModel
)
{
    val messages by viewModel.messages.collectAsState()
    val messageListScrollState = rememberLazyListState()

    // Load messages when ChatPage appears
    LaunchedEffect(viewModel.chatId)
    {
        viewModel.loadMessages()
    }

    // Scroll to the bottom when messages are loaded / new messages added
    LaunchedEffect(key1 = messages)
    {
        if (messages.isNotEmpty())
            messageListScrollState.animateScrollToItem(messages.size - 1)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LazyColumn(modifier = Modifier.weight(1f), state = messageListScrollState) {
            items(messages) { message ->
                MessageCard(message, viewModel.currentUserId == message.senderId)
                Spacer(modifier = Modifier.height(1.dp))
            }
        }

        OutlinedTextField(
            value = viewModel.getMessage(),
            onValueChange = { viewModel.setMessage(it) },
            label = { Text("Message...") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        Button(
            onClick = { viewModel.sendMessage() },
            modifier = Modifier
                .align(Alignment.End)
                .padding(8.dp)
        ) {
            Text("Send")
        }
    }
}

@Composable
fun MessageCard(message: Message, fromCurrentUser: Boolean)
{
    Row(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth(),
        horizontalArrangement = if (fromCurrentUser) Arrangement.End else Arrangement.Start
    ) {
        Card(
            shape = RoundedCornerShape(10.dp),
            elevation = CardDefaults.cardElevation(0.5.dp),
            modifier = Modifier
                .wrapContentWidth()
                .widthIn(max = LocalConfiguration.current.screenWidthDp.dp * 0.7f)
        ) {
            BoxWithConstraints {
                Column(
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(
                        text = message.content,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = TextUnit(17f, TextUnitType.Sp)
                        )
                    )
                    Text(
                        text = SimpleDateFormat(
                            "HH:mm",
                            Locale.getDefault()
                        ).format(message.sentAt),
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.align(Alignment.End)
                    )
                }
            }
        }
    }
}

package me.bnnq.messenger.components.conversations

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import me.bnnq.messenger.R
import me.bnnq.messenger.models.Chat

@Composable
fun ConversationsPage(
    navController: NavController,
    viewModel: ConversationsViewModel = hiltViewModel()
)
{
    val chats by viewModel.chats.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LazyColumn {
            items(chats) { chat ->
                Box(modifier = Modifier.clickable { navController.navigate("chat/${chat.id}") })
                {
                    ChatCard(chat)
                }
            }
        }

    }
}

@Composable
fun ChatCard(chat: Chat)
{
    Card(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(10.dp),
    )
    {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.Top
        )
        {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data("https://picsum.photos/200/300")
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.ic_launcher_foreground),
                contentDescription = "Avatar",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clip(CircleShape)
                    .width(59.dp)
                    .height(59.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = "Chat ${chat.id}",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontSize = TextUnit(
                            19f,
                            TextUnitType.Sp
                        ),
                        fontWeight = FontWeight.W500
                    )
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(text = "Last message", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
package me.bnnq.messenger.components.conversations

import android.annotation.SuppressLint
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import me.bnnq.messenger.models.dto.ChatInfoDto
import me.bnnq.messenger.services.ServerCommunicationPool

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConversationsPage(
    navController: NavController,
    viewModel: ConversationsViewModel = hiltViewModel()
)
{
    val newMessageChat by viewModel.newMessageChat.collectAsState()

    DisposableEffect(viewModel) {
        viewModel.onInit()
        onDispose { viewModel.onDispose() }
    }

    val chats by viewModel.chats.collectAsState()
    // Add this state to control the visibility of the dialog
    var showDialog by remember { mutableStateOf(false) }

    // Add this state to hold the input username
    var newChatUserUsername by remember { mutableStateOf("") }

    // Add this state to control the visibility of the avatar update dialog
    var showAvatarDialog by remember { mutableStateOf(false) }

    val avatarUrl by viewModel.avatarUrl.observeAsState()

    // Add this launcher to select an image from the gallery
    val galleryLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                viewModel.updateAvatar(it)
                showAvatarDialog = false
            }
        }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(avatarUrl)
                                .crossfade(true)
                                .build(),
                            placeholder = painterResource(R.drawable.ic_launcher_foreground),
                            contentDescription = "Avatar",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .clip(CircleShape)
                                .width(38.dp)
                                .height(38.dp)
                                .clickable {
                                    showAvatarDialog =
                                        true
                                } // Open the avatar update dialog on click
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(ServerCommunicationPool.currentUser?.username ?: "")
                    }
                }
            )
        },
        content = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                LazyColumn {
                    items(chats) { chat ->
                        Box(modifier = Modifier.clickable { navController.navigate("chat/${chat.id}") })
                        {
                            ChatCard(chat, chat == newMessageChat)
                        }
                    }
                }

                FloatingActionButton(
                    onClick = { showDialog = true },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp)
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "Add")
                }

                // Add this dialog
                if (showDialog)
                {
                    AlertDialog(
                        onDismissRequest = { showDialog = false },
                        title = { Text(text = "Enter username") },
                        text = {
                            OutlinedTextField(
                                value = newChatUserUsername,
                                onValueChange = { newChatUserUsername = it },
                                label = { Text("Username") }
                            )
                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    viewModel.createChat(newChatUserUsername)
                                    showDialog = false
                                }
                            ) {
                                Text("Ok")
                            }
                        })
                }
            }
        }
    )
    if (showAvatarDialog)
    {
        AlertDialog(
            onDismissRequest = { showAvatarDialog = false },
            title = { Text(text = "Update avatar") },
            text = { Text("Would you like to select a new avatar image from the gallery?") },
            confirmButton = {
                Button(
                    onClick = {
                        galleryLauncher.launch("image/*") // Launch the gallery when the button is clicked
                    }
                ) {
                    Text("Select Image")
                }
            },
            dismissButton = {
                Button(onClick = { showAvatarDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

}

@Composable
fun ChatCard(chat: ChatInfoDto, isHighlighted: Boolean)
{
    Box(modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)) {
        Card(
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            elevation = CardDefaults.cardElevation(5.dp),
        )
        {
            Row(
                modifier = Modifier.padding(8.dp),
                verticalAlignment = Alignment.Top
            )
            {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(chat.users.first { it.id != ServerCommunicationPool.currentUser!!.id }.avatarImagePath)
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
                        text = chat.users.first { it.id != ServerCommunicationPool.currentUser!!.id }.username,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontSize = TextUnit(
                                19f,
                                TextUnitType.Sp
                            ),
                            fontWeight = FontWeight.W500
                        )
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(text = chat.lastMessage?.text ?: "", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }

        if (isHighlighted)
        {
            Box(
                modifier = Modifier
                    .offset(y = (-4).dp, x = (-4).dp)
                    .size(15.dp)
                    .background(Color(67, 183, 222), CircleShape)
            )
        }
    }
}
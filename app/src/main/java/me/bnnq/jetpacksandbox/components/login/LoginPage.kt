package me.bnnq.jetpacksandbox.components.login

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import me.bnnq.jetpacksandbox.R

@Composable
fun LoginPage(
    context: Context,
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val loginResult by viewModel.loginResult.collectAsState()

    loginResult?.let {
        if (it.success)
            navController.navigate("conversations")
        else
            Toast.makeText(context, it.errorMessage, Toast.LENGTH_SHORT).show()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Card(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(16.dp)
                .shadow(30.dp),
            shape = RoundedCornerShape(10.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 20.dp
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Login", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = viewModel.getUsername(),
                    onValueChange = { viewModel.setUsername(it) },
                    label = { Text("Username") },
                    maxLines = 3,
                    isError = viewModel.usernameValidationError != null,
                    supportingText = { Text(viewModel.usernameValidationError ?: "") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = viewModel.getPassword(),
                    onValueChange = { viewModel.setPassword(it) },
                    label = { Text( "Password") },
                    maxLines = 3,
                    isError = viewModel.passwordValidationError != null,
                    supportingText = { Text(viewModel.passwordValidationError ?: "") }
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { viewModel.login() }) {
                    Text("LOGIN")
                }
                Spacer(modifier = Modifier.height(16.dp))
                ClickableText(
                    text = AnnotatedString("Don't have an account? Register"),
                    onClick = { navController.navigate("register") },
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color(73, 93, 146), textDecoration = TextDecoration.Underline))
            }
        }
    }
}
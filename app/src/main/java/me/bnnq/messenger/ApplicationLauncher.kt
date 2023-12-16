package me.bnnq.messenger

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import me.bnnq.messenger.components.chat.ChatPage
import me.bnnq.messenger.components.chat.ChatViewModel
import me.bnnq.messenger.components.conversations.ConversationsPage
import me.bnnq.messenger.components.login.LoginPage
import me.bnnq.messenger.components.register.RegisterPage
import me.bnnq.messenger.components.splash.SplashPage
import me.bnnq.messenger.services.TitlePageBackHandler
import me.bnnq.messenger.services.abstractions.IBackHandler

@Composable
fun ApplicationLauncher(activity: MainActivity, context: Context)
{
    val navController = rememberNavController()
    val backTrackHandler: IBackHandler = TitlePageBackHandler(activity, context)

    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") { SplashPage(navController) }
        composable("login") {
            BackHandler {
                backTrackHandler.handleBackPress()
            }

            LoginPage(context, navController)
        }
        composable("register") {
            BackHandler {
                backTrackHandler.handleBackPress()
            }

            RegisterPage(context, navController)
        }
        composable("conversations") {
            BackHandler {
                backTrackHandler.handleBackPress()
            }

            ConversationsPage(navController)
        }
        composable("chat/{chatId}") { backStackEntry ->
            val chatId = backStackEntry.arguments?.getString("chatId")?.toInt()
            if (chatId != null)
            {
                val viewModel: ChatViewModel = hiltViewModel()
                ChatPage(viewModel = viewModel)
            }
        }
    }
}
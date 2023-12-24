package me.bnnq.messenger

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import me.bnnq.messenger.services.ServerCommunicationPool
import me.bnnq.messenger.ui.theme.JetpackSandboxTheme
import kotlin.coroutines.CoroutineContext

@AndroidEntryPoint
class MainActivity : ComponentActivity(), CoroutineScope
{
    private lateinit var job: Job

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        job = Job()
        val instance = FirebaseMessaging.getInstance()
        instance.token.addOnCompleteListener { task ->
            if (!task.isSuccessful)
            {
                Log.w("ServerCommunicationPool", "Fetching FCM registration token failed", task.exception)
                return@addOnCompleteListener
            }

            ServerCommunicationPool.updateFcmTokenOnServer(task.result!!)

            Log.d("ServerCommunicationPool", "FCM registration token: ${task.result!!}")
        }

        setContent {
            JetpackSandboxTheme {
                ApplicationLauncher(this, applicationContext)
            }
        }
    }

    override fun onDestroy()
    {
        super.onDestroy()
        job.cancel()
    }
}
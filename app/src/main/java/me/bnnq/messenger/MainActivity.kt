package me.bnnq.messenger

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
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
package me.bnnq.jetpacksandbox.services

import android.app.Activity
import android.content.Context
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.bnnq.jetpacksandbox.services.abstractions.IBackHandler

class TitlePageBackHandler(
    private val exitableActivity: CoroutineScope,
    private val context: Context
) : IBackHandler
{
    private var isBackPressedOnce = false

    override fun handleBackPress()
    {
        if (isBackPressedOnce)
        {
            (exitableActivity as Activity).finish()
        } else
        {
            isBackPressedOnce = true
            Toast.makeText(context, "Press back again to exit", Toast.LENGTH_SHORT).show()

            exitableActivity.launch {
                delay(5000)
                isBackPressedOnce = false
            }

        }
    }

}
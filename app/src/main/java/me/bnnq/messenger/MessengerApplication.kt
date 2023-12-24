package me.bnnq.messenger

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MessengerApplication : Application()
{
    companion object
    {
        lateinit var instance: MessengerApplication
            private set
    }

    override fun onCreate()
    {
        super.onCreate()
        instance = this
    }
}
package me.bnnq.messenger.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import java.io.Closeable

class ServiceCoroutineScope(dispatcher: CoroutineDispatcher) : Closeable
{
    private val job = Job()
    private val scope: CoroutineScope

    init
    {
        scope = CoroutineScope(dispatcher + job)
    }

    override fun close()
    {
        job.cancel()
    }

}
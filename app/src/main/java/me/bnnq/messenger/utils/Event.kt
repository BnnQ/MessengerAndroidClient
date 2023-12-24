package me.bnnq.messenger.utils

import java.util.concurrent.ConcurrentLinkedQueue

class Event<T>
{
    private var lastValue: T? = null
    private val handlers = ConcurrentLinkedQueue<(T) -> Unit>()

    operator fun plusAssign(handler: (T) -> Unit)
    {
        handlers.add(handler)
    }

    operator fun minusAssign(handler: (T) -> Unit)
    {
        handlers.remove(handler)
    }

    operator fun invoke(value: T)
    {
        val handlersCopy = handlers.toList()
        for (handler in handlersCopy)
        {
            handler(value)
        }

        lastValue = value
    }

    fun subscribeOnce(handler: (T) -> Unit)
    {
        handlers.add(object : (T) -> Unit
        {
            override fun invoke(item: T)
            {
                handler(item)
                handlers.remove(this)
            }
        })
    }

    fun subscribeOnceWithCondition(handler: (T) -> Unit, condition: (T) -> Boolean)
    {
        handlers.add(object : (T) -> Unit
        {
            override fun invoke(item: T)
            {
                if (condition(item))
                {
                    handler(item)
                    handlers.remove(this)
                }
            }
        })
    }

    fun subscribeOnceImmediately(handler: (T) -> Unit)
    {
        if (lastValue != null)
        {
            handler(lastValue!!)
        }
        else
        {
            subscribeOnce(handler)
        }
    }
}

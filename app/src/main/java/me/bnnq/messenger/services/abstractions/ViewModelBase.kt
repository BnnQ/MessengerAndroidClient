package me.bnnq.messenger.services.abstractions

import androidx.lifecycle.ViewModel

abstract class ViewModelBase : ViewModel()
{
    abstract fun onInit();
    abstract fun onDispose();
}
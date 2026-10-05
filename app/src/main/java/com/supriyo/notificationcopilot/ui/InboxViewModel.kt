package com.supriyo.notificationcopilot.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.supriyo.notificationcopilot.data.AppDatabase
import com.supriyo.notificationcopilot.data.ListenerEventEntity
import com.supriyo.notificationcopilot.data.NotificationEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class InboxViewModel(application: Application) : AndroidViewModel(application) {
    private val notificationDao = AppDatabase
        .getInstance(application)
        .notificationDao()
    private val listenerEventDao = AppDatabase
        .getInstance(application)
        .listenerEventDao()

    val notifications: StateFlow<List<NotificationEntity>> = notificationDao
        .getAllNotifications()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    val listenerEvents: StateFlow<List<ListenerEventEntity>> = listenerEventDao
        .getAllEvents()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )
}

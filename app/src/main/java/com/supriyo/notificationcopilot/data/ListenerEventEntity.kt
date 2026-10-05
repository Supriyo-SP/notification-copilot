package com.supriyo.notificationcopilot.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "listener_events")
data class ListenerEventEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val type: String,
    val timestamp: Long
)

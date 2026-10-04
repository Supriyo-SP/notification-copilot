package com.supriyo.notificationcopilot.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val key: String,
    val packageName: String,
    val title: String?,
    val text: String?,
    val bigText: String?,
    val postTime: Long,
    val capturedAt: Long
)

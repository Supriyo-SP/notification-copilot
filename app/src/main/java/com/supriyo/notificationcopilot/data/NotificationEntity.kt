package com.supriyo.notificationcopilot.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room3.PrimaryKey

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
    val capturedAt: Long,
    val category: String = "OTHER",
    val subcategory: String = "NONE",
    val important: Boolean = false
)

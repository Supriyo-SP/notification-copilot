package com.supriyo.notificationcopilot.data

data class CapturedNotification(
    val packageName: String,
    val title: String?,
    val text: String?,
    val bigText: String?,
    val postTime: Long,
    val key: String,
    val isOngoing: Boolean,
    val isGroupSummary: Boolean
)

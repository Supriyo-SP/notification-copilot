package com.supriyo.notificationcopilot.service

import android.app.Notification
import android.content.pm.ApplicationInfo
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.supriyo.notificationcopilot.data.CapturedNotification
import com.supriyo.notificationcopilot.data.AppDatabase
import com.supriyo.notificationcopilot.data.NotificationEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class NotificationCaptureService : NotificationListenerService() {
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val notificationDao by lazy {
        AppDatabase.getInstance(applicationContext).notificationDao()
    }

    override fun onListenerConnected() {
        super.onListenerConnected()
        Log.d(TAG, "Notification listener connected")
    }

    override fun onListenerDisconnected() {
        super.onListenerDisconnected()
        Log.d(TAG, "Notification listener disconnected")
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        val notification = sbn.notification
        val isOngoing = notification.flags and Notification.FLAG_ONGOING_EVENT != 0
        val isGroupSummary = notification.flags and Notification.FLAG_GROUP_SUMMARY != 0

        if (
            isOngoing ||
            isGroupSummary ||
            sbn.packageName == packageName ||
            sbn.packageName in EXCLUDED_PACKAGES
        ) {
            return
        }

        val extras = notification.extras
        val capturedNotification = CapturedNotification(
            packageName = sbn.packageName,
            title = extras.getCharSequence(Notification.EXTRA_TITLE)?.toString(),
            text = extras.getCharSequence(Notification.EXTRA_TEXT)?.toString(),
            bigText = extras.getCharSequence(Notification.EXTRA_BIG_TEXT)?.toString(),
            postTime = sbn.postTime,
            key = sbn.key,
            isOngoing = isOngoing,
            isGroupSummary = isGroupSummary
        )

        serviceScope.launch {
            notificationDao.insert(
                NotificationEntity(
                    key = capturedNotification.key,
                    packageName = capturedNotification.packageName,
                    title = capturedNotification.title,
                    text = capturedNotification.text,
                    bigText = capturedNotification.bigText,
                    postTime = capturedNotification.postTime,
                    capturedAt = System.currentTimeMillis()
                )
            )
        }

        val isDebuggable = applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
        if (isDebuggable) {
            Log.d(TAG, "Captured notification: $capturedNotification")
        } else {
            Log.d(
                TAG,
                "Captured notification from package=${capturedNotification.packageName}, " +
                    "postTime=${capturedNotification.postTime}, key=${capturedNotification.key}"
            )
        }
    }

    override fun onDestroy() {
        serviceScope.cancel()
        super.onDestroy()
    }

    private companion object {
        const val TAG = "NotifCapture"

        // Add sensitive or unwanted notification package names here later.
        val EXCLUDED_PACKAGES = emptySet<String>()
    }
}

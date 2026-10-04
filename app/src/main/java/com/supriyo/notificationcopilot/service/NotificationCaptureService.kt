package com.supriyo.notificationcopilot.service

import android.app.Notification
import android.content.pm.ApplicationInfo
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.supriyo.notificationcopilot.data.CapturedNotification

class NotificationCaptureService : NotificationListenerService() {
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

        if (isOngoing || isGroupSummary || sbn.packageName == packageName) {
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

    private companion object {
        const val TAG = "NotifCapture"
    }
}

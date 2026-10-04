package com.supriyo.notificationcopilot.util

import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.core.app.NotificationManagerCompat

object PermissionUtils {
    fun isNotificationAccessGranted(context: Context): Boolean {
        return context.packageName in NotificationManagerCompat
            .getEnabledListenerPackages(context)
    }

    fun openNotificationAccessSettings(context: Context) {
        context.startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
    }
}

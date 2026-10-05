package com.supriyo.notificationcopilot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.supriyo.notificationcopilot.data.NotificationEntity
import com.supriyo.notificationcopilot.ui.InboxViewModel
import com.supriyo.notificationcopilot.ui.theme.NotificationCopilotTheme
import com.supriyo.notificationcopilot.util.PermissionUtils
import kotlinx.coroutines.flow.StateFlow
import java.text.DateFormat
import java.util.Date

class MainActivity : ComponentActivity() {
    private var notificationAccessGranted by mutableStateOf(false)
    private val inboxViewModel by lazy {
        ViewModelProvider(this)[InboxViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NotificationCopilotTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NotificationAccessScreen(
                        notificationAccessGranted = notificationAccessGranted,
                        notifications = inboxViewModel.notifications,
                        onOpenSettings = {
                            PermissionUtils.openNotificationAccessSettings(this)
                        },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        notificationAccessGranted = PermissionUtils.isNotificationAccessGranted(this)
    }
}

@Composable
private fun NotificationAccessScreen(
    notificationAccessGranted: Boolean,
    notifications: StateFlow<List<NotificationEntity>>,
    onOpenSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    val notificationList by notifications.collectAsStateWithLifecycle()

    Column(
        modifier = modifier.fillMaxSize().padding(24.dp)
    ) {
        Text(text = "Notification Copilot")
        if (notificationAccessGranted) {
            Text(
                text = "Listening to notifications",
                modifier = Modifier.padding(top = 16.dp)
            )
        } else {
            Text(
                text = "Notification access is off, the app can't see your notifications",
                modifier = Modifier.padding(top = 16.dp)
            )
            Button(
                onClick = onOpenSettings,
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("Grant access")
            }
        }
        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
        LazyColumn(
            modifier = Modifier.fillMaxWidth().weight(1f)
        ) {
            items(
                items = notificationList,
                key = { notification -> notification.id }
            ) { notification ->
                NotificationRow(notification)
            }
        }
    }
}

@Composable
private fun NotificationRow(notification: NotificationEntity) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)
    ) {
        Text(text = notification.packageName)
        Text(text = notification.title ?: "(no title)")
        Text(text = notification.text ?: "(no text)")
        Text(
            text = DateFormat.getDateTimeInstance().format(Date(notification.postTime)),
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NotificationAccessScreenPreview() {
    NotificationCopilotTheme {
        NotificationAccessScreen(
            notificationAccessGranted = false,
            notifications = kotlinx.coroutines.flow.MutableStateFlow<List<NotificationEntity>>(emptyList()),
            onOpenSettings = {}
        )
    }
}
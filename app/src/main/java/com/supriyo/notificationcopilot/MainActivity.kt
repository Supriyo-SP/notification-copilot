package com.supriyo.notificationcopilot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.supriyo.notificationcopilot.ui.theme.NotificationCopilotTheme
import com.supriyo.notificationcopilot.util.PermissionUtils

class MainActivity : ComponentActivity() {
    private var notificationAccessGranted by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NotificationCopilotTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NotificationAccessScreen(
                        notificationAccessGranted = notificationAccessGranted,
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
    onOpenSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Notification Copilot")
        Text(
            text = if (notificationAccessGranted) {
                "Notification access: granted"
            } else {
                "Notification access: not granted"
            },
            modifier = Modifier.padding(top = 16.dp)
        )
        Button(
            onClick = onOpenSettings,
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Open notification access settings")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun NotificationAccessScreenPreview() {
    NotificationCopilotTheme {
        NotificationAccessScreen(
            notificationAccessGranted = false,
            onOpenSettings = {}
        )
    }
}
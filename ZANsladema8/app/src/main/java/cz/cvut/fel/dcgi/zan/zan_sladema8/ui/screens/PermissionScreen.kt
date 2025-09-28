package cz.cvut.fel.dcgi.zan.zan_sladema8.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cz.cvut.fel.dcgi.zan.zan_sladema8.ui.components.MainBottomNavigation
import cz.cvut.fel.dcgi.zan.zan_sladema8.ui.navigation.BottomNavigationItem
import cz.cvut.fel.dcgi.zan.zan_sladema8.util.PermissionManager

@Composable
fun PermissionScreen(
    mainBottomNavigationItems: List<BottomNavigationItem>,
    currentDestination: String?,
    onPermissionsGranted: () -> Unit,
    onRequestNotificationPermission: () -> Unit,
    onRequestAlarmPermission: () -> Unit
) {
    Scaffold(
        bottomBar = {
            MainBottomNavigation(mainBottomNavigationItems, currentDestination)
        },
        modifier = Modifier.fillMaxSize(),
    ) { innerPadding ->
        PermissionContent(
            onPermissionsGranted = onPermissionsGranted,
            onRequestNotificationPermission = onRequestNotificationPermission,
            onRequestAlarmPermission = onRequestAlarmPermission,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PermissionContent(
    onPermissionsGranted: () -> Unit,
    onRequestNotificationPermission: () -> Unit,
    onRequestAlarmPermission: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var notificationPermissionGranted by remember {
        mutableStateOf(PermissionManager.hasNotificationPermission(context))
    }
    var alarmPermissionGranted by remember {
        mutableStateOf(PermissionManager.hasAlarmPermission(context))
    }

    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(1000)
        notificationPermissionGranted = PermissionManager.hasNotificationPermission(context)
        alarmPermissionGranted = PermissionManager.hasAlarmPermission(context)

        if (notificationPermissionGranted && alarmPermissionGranted) {
            onPermissionsGranted()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "🏋️ Workout Reminders",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "To help you stay on track with your fitness goals, we need a couple of permissions:",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(32.dp))

        PermissionCard(
            title = "Notifications",
            description = "Get reminded about your scheduled workouts",
            icon = Icons.Default.Notifications,
            isGranted = notificationPermissionGranted,
            onRequest = onRequestNotificationPermission
        )

        Spacer(modifier = Modifier.height(16.dp))

        PermissionCard(
            title = "Exact Alarms",
            description = "Schedule workout reminders at precise times",
            icon = Icons.Default.Schedule,
            isGranted = alarmPermissionGranted,
            onRequest = onRequestAlarmPermission
        )

        Spacer(modifier = Modifier.height(32.dp))

        if (notificationPermissionGranted && alarmPermissionGranted) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "All permissions granted! You're ready to go.",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onPermissionsGranted,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Continue to App")
            }
        }
    }
}

@Composable
fun PermissionCard(
    title: String,
    description: String,
    icon: ImageVector,
    isGranted: Boolean,
    onRequest: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isGranted)
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
            else
                MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = if (isGranted) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(32.dp)
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                if (isGranted) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = "Permission granted",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Icon(
                        Icons.Default.Error,
                        contentDescription = "Permission required",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            if (!isGranted) {
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onRequest,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Grant Permission")
                }
            }
        }
    }
}

@Composable
fun PermissionRationaleDialog(
    permissionName: String,
    explanation: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Permission Required",
                style = MaterialTheme.typography.headlineSmall
            )
        },
        text = {
            Column {
                Text(
                    text = "We need access to $permissionName to provide the best experience.",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = explanation,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Grant Permission")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Not Now")
            }
        }
    )
}
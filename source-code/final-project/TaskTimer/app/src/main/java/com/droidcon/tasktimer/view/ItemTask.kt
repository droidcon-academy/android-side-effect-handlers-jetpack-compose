package com.droidcon.tasktimer.view

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.droidcon.tasktimer.R
import com.droidcon.tasktimer.notification.TaskNotificationService
import com.droidcon.tasktimer.model.Task
import kotlinx.coroutines.delay

@Composable
fun ItemTask(
    task: Task,
    taskNotificationService: TaskNotificationService,
    updateTask: (Task) -> Unit,
) {

    val totalSeconds =
        (task.durationHours * 3600 + task.durationMinutes * 60 + task.durationSeconds).toLong()

    var isRunning by remember { mutableStateOf(false) }
    var isPaused by remember { mutableStateOf(false) }
    var isReset by remember { mutableStateOf(false) }

    var remainingTime by remember { mutableStateOf(totalSeconds) }
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val lifecycleObserver = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    if (isPaused) {
                        isRunning = true
                        isPaused = false
                    }
                }
                Lifecycle.Event.ON_PAUSE -> {
                    if (isRunning) {
                        isRunning = false
                        isPaused = true
                    }
                }
                else -> {}
            }
        }

        lifecycleOwner.lifecycle.addObserver(lifecycleObserver)

        onDispose {
            Log.d("TaskTimerApp", "DisposableEffect Disposed")
            lifecycleOwner.lifecycle.removeObserver(lifecycleObserver)
        }
    }

    // Use LaunchedEffect to perform side effects whenever a particular condition is met, such as starting or stopping a timer.
    LaunchedEffect(isRunning) {
        if (isRunning) {
            while (remainingTime > 0) {
                delay(1000)
                remainingTime--
            }
            isRunning = false // Stop the timer when it reaches zero

            taskNotificationService.showBasicNotification(task.name)
        }
    }
    if (isReset) {
        remainingTime = totalSeconds
    }

    //Create a snapshotFlow that observes changes in the remainingTime.
    val remainingTimeFlow = snapshotFlow { remainingTime }

    //We can use snapshotFlow to observe the timer’s state and react whenever it updates.
    LaunchedEffect(Unit) {
        remainingTimeFlow.collect { time ->
            Log.d(
                "TaskTimerApp",
                "Remaining time for task: ${time}"
            )
        }
    }

    Card(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {

        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            Column(modifier = Modifier) {

                Text(text = task.name)

                Spacer(modifier = Modifier.padding(bottom = 4.dp))

                Text(
                    text = formatTime(remainingTime),
                    color = if (isRunning) MaterialTheme.colorScheme.primary else Color.Black,
                    fontWeight = if (isRunning) FontWeight.Bold else FontWeight.SemiBold,
                    fontSize = 18.sp
                )
            }

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
            if (isRunning) {
                IconButton(onClick = {
                    isRunning = !isRunning
                    task.status = "Paused"
                    updateTask(task)

                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_pause_circle),
                        contentDescription = "",
                        Modifier.size(30.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            } else {
                IconButton(onClick = {
                    isRunning = !isRunning
                    if (isReset) {
                        isReset = false
                    }
                    task.status = "Ongoing"
                    updateTask(task)
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_play),
                        contentDescription = "",
                        Modifier.size(30.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }


            IconButton(onClick = {
                isRunning = false
                isReset = true
                //remainingTime.value = totalSeconds
            }) {
                Icon(
                    Icons.Outlined.Refresh,
                    contentDescription = "",
                    Modifier.size(30.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }


        }
    }

}


@Composable
fun formatTime(remainingTime: Long): String {
    val hours = (remainingTime / 3600).toInt()
    val minutes = ((remainingTime % 3600) / 60).toInt()
    val seconds = (remainingTime % 60).toInt()

    return if (hours == 0 && minutes != 0) {
        String.format("%02dm %02ds", minutes, seconds)
    } else if (hours == 0 && minutes == 0) {
        String.format("%02ds", seconds)
    } else {
        String.format("%02dh %02dm %02ds", hours, minutes, seconds)
    }
}

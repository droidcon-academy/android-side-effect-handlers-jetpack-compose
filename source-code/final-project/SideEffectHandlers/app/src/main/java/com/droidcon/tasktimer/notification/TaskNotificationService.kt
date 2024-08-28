package com.droidcon.tasktimer.notification

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.droidcon.tasktimer.R
import kotlin.random.Random

class TaskNotificationService(private val context: Context) {
    private val notificationManager=context.getSystemService(NotificationManager::class.java)

    fun showBasicNotification(taskName:String){
        val notification= NotificationCompat.Builder(context,"task_notification")
            .setContentTitle(taskName)
            .setContentText("Task Completed")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationManager.IMPORTANCE_HIGH)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(
            Random.nextInt(),
            notification
        )
    }

}
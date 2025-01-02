package com.droidcon.tasktimer.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task")
data class Task(
    @PrimaryKey(autoGenerate = true) val taskId: Long = 0,
    var name: String,
    var durationHours: Int,
    var durationMinutes: Int,
    var durationSeconds:Int,
    var status:String
)

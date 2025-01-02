package com.droidcon.tasktimer.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.droidcon.tasktimer.model.Task

@Database(entities = [Task::class], version = 1)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}
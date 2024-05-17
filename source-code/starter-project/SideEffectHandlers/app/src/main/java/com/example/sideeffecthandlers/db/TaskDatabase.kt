package com.example.sideeffecthandlers.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.sideeffecthandlers.model.Task

@Database(entities = [Task::class], version = 1)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}
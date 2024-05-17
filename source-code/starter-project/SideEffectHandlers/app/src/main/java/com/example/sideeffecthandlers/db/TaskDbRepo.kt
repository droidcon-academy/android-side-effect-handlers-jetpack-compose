package com.example.sideeffecthandlers.db

import com.example.sideeffecthandlers.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskDbRepo {
    suspend fun getTasksFromRepo(): Flow<List<Task>>

    suspend fun addTaskToRepo(task: Task)

    suspend fun updateTaskInRepo(task: Task)

}
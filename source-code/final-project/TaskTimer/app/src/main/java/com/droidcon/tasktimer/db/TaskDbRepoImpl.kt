package com.droidcon.tasktimer.db

import com.droidcon.tasktimer.model.Task
import kotlinx.coroutines.flow.Flow

class TaskDbRepoImpl(private val taskDao: TaskDao):TaskDbRepo {

    override suspend fun getTasksFromRepo(): Flow<List<Task>> {
        return taskDao.getAllTasks()
    }

    override suspend fun addTaskToRepo(task: Task) {
        taskDao.addTask(task)
    }


    override suspend fun updateTaskInRepo(task: Task) {
        taskDao.updateTask(task)
    }


}
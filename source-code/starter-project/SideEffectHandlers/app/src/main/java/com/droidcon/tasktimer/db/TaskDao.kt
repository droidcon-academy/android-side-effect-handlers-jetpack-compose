package com.droidcon.tasktimer.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.droidcon.tasktimer.model.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Insert
    suspend fun addTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

    @Query("SELECT * FROM task")
    fun getAllTasks(): Flow<List<Task>>

}
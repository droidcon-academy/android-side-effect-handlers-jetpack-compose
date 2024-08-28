package com.droidcon.tasktimer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.droidcon.tasktimer.model.Task
import com.droidcon.tasktimer.db.TaskDbRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(private val taskDbRepo: TaskDbRepo) : ViewModel() {

    val tasks = MutableStateFlow<List<Task>>(listOf())

    init {
        getTasks()
    }

     fun getTasks() {
        viewModelScope.launch {
            taskDbRepo.getTasksFromRepo().collect {
                tasks.value = it
            }
        }
    }

     fun addTask(task: Task){
        viewModelScope.launch {
            taskDbRepo.addTaskToRepo(task)
        }
    }
     fun updateTask(task: Task){
        viewModelScope.launch {
            taskDbRepo.updateTaskInRepo(task)
        }
    }
}


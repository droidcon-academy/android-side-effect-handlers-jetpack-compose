package com.example.sideeffecthandlers.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sideeffecthandlers.model.Task
import com.example.sideeffecthandlers.db.TaskDbRepo
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

/*
@HiltViewModel
class TaskDbViewModel @Inject constructor(private val repo: CollectionDbRepo) : ViewModel() {
    */
/*val collection = MutableStateFlow<List<DbBook>>(listOf())
    val currentBook = MutableStateFlow<DbBook?>(null)

    init {
        getCollection()
    }

    private fun getCollection() {
        viewModelScope.launch {
            repo.getBooksFromRepo().collect {
                collection.value = it
            }
        }
    }

    fun setCurrentBookId(bookId: String?) {
        bookId?.let {
            viewModelScope.launch {
                repo.getBookFromRepo(it).collect {
                    currentBook.value = it
                }
            }
        }
    }

    fun addBook(book: Volume) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.addBookToRepo(DbBook.fromVolume(book))
        }
    }

    fun deleteBook(book: DbBook) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteBookFromRepo(book)
        }
    }
*//*

}*/

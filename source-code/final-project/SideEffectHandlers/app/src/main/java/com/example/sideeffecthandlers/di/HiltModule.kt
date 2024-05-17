package com.example.sideeffecthandlers.di

import android.content.Context
import androidx.room.Room
import com.example.sideeffecthandlers.db.TaskDatabase
import com.example.sideeffecthandlers.db.TaskDao
import com.example.sideeffecthandlers.db.TaskDbRepo
import com.example.sideeffecthandlers.db.TaskDbRepoImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ViewModelComponent::class)
class HiltModule {

    @Provides
    fun provideTaskDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, TaskDatabase::class.java, "TaskDB").build()

    @Provides
    fun provideTaskDao(taskDatabase: TaskDatabase) = taskDatabase.taskDao()

    @Provides
    fun provideDbRepo(taskDao: TaskDao): TaskDbRepo = TaskDbRepoImpl(taskDao)

}

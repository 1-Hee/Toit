package com.one.toit.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.one.toit.data.TaskDataBase
import com.one.toit.data.model.Task
import com.one.toit.data.repository.TaskInfoRepository
import com.one.toit.data.repository.TaskRepository

class TaskViewModel(
    private val application: Application
) : AndroidViewModel(application) {
    //db init
    private val repository:TaskRepository
    init {
        val taskDao = TaskDataBase
                        .getInstance(application.applicationContext)
                        .TaskDao()
        repository = TaskRepository(taskDao)
    }

    // read
    suspend fun readTaskList():List<Task>{
        return repository.readTaskList()
    }

}
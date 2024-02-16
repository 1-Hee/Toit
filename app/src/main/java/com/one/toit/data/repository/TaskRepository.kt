package com.one.toit.data.repository

import com.one.toit.data.dao.TaskDao
import com.one.toit.data.model.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TaskRepository(
    private val taskDao:TaskDao
) {
    suspend fun readTaskList():List<Task> = withContext(Dispatchers.IO){
        return@withContext taskDao.readTaskList()
    }
}
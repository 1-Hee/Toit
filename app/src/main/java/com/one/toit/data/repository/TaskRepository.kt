package com.one.toit.data.repository

import com.one.toit.data.dao.TaskDao
import com.one.toit.data.model.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Date

class TaskRepository(
    private val taskDao:TaskDao
) {
    suspend fun readTaskList():List<Task> = withContext(Dispatchers.IO){
        return@withContext taskDao.readTaskList()
    }
    suspend fun readNotCompleteTaskList():List<Task> = withContext(Dispatchers.IO){
        return@withContext taskDao.readNotCompleteTaskList()
    }

    suspend fun readTaskListByDate(targetDate: Date): List<Task> = withContext(Dispatchers.IO){
        return@withContext taskDao.readTaskListByDate(targetDate)
    }

    suspend fun readNotCompleteTaskListByDate(targetDate: Date): List<Task> = withContext(Dispatchers.IO){
        return@withContext taskDao.readNotCompleteTaskListByDate(targetDate)
    }
}
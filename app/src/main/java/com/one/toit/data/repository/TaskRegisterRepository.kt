package com.one.toit.data.repository

import androidx.lifecycle.LiveData
import com.one.toit.data.dao.TaskRegistrationDao
import com.one.toit.data.model.TaskRegister
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class TaskRegisterRepository(
    private val taskRegistrationDao: TaskRegistrationDao
) {
    // create
    suspend fun addTaskRegister(taskRegister: TaskRegister)
        = withContext(Dispatchers.IO) {
        Timber.d("addTaskRegister.. %s", taskRegister)
        taskRegistrationDao.addTaskRegister(taskRegister)
    }
    suspend fun addAllTaskRegister(vararg taskRegisters: TaskRegister)
        = withContext(Dispatchers.IO){
        Timber.d("addAllTaskRegister.. %s", taskRegisters)
        taskRegistrationDao.addAllTaskRegister(*taskRegisters)
    }
    // read
    suspend fun readTaskRegisterList(): LiveData<List<TaskRegister>>
        = withContext(Dispatchers.IO){
        Timber.d("readTaskRegisterList...")
        taskRegistrationDao.readTaskRegisterList()
    }
    // update
    suspend fun modifyTaskRegister(taskRegister: TaskRegister)
        = withContext(Dispatchers.IO){
        Timber.d("modifyTaskRegister... %s", taskRegister)
        taskRegistrationDao.modifyTaskRegister(taskRegister)
    }
    // delete
    suspend fun removeTaskRegister(taskRegister: TaskRegister)
        = withContext(Dispatchers.IO){
        Timber.d("removeTaskRegister... %s", taskRegister)
        taskRegistrationDao.removeTaskRegister(taskRegister)
    }

    suspend fun removeTaskRegisterList(vararg taskRegisters: TaskRegister)
        = withContext(Dispatchers.IO){
        Timber.d("removeTaskRegisters... %s", taskRegisters)
        taskRegistrationDao.removeTaskRegisterList(*taskRegisters)
    }

    suspend fun clearAll()
        = withContext(Dispatchers.IO){
        Timber.d("clearAll...")
        taskRegistrationDao.clearAll()
    }

    suspend fun deleteAll()
        = withContext(Dispatchers.IO){
        Timber.d("deleteAll...")
        taskRegistrationDao.deleteAll()
    }
}
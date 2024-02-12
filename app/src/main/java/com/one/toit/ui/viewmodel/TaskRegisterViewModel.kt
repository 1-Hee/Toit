package com.one.toit.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.one.toit.data.TaskRegistrationBase
import com.one.toit.data.model.TaskRegister
import com.one.toit.data.repository.TaskRegisterRepository
import timber.log.Timber

class TaskRegisterViewModel(
    private val application: Application
) : AndroidViewModel(application) {
    // db init
    private val repository:TaskRegisterRepository
    init {
        val taskRegistrationDao = TaskRegistrationBase
            .getInstance(application.applicationContext)
            .TaskRegistrationDao()
        repository = TaskRegisterRepository(taskRegistrationDao)
    }
    // create
    suspend fun addTaskRegister(taskRegister: TaskRegister){
        Timber.d("")
        repository.addTaskRegister(taskRegister)
    }
    suspend fun addAllTaskRegister(vararg taskRegisters : TaskRegister){
        repository.addAllTaskRegister(*taskRegisters)
    }
    // read
    suspend fun readTaskRegisterList():LiveData<List<TaskRegister>>{
        return repository.readTaskRegisterList()
    }
    // update
    suspend fun modifyTaskRegister(taskRegister: TaskRegister){
        repository.modifyTaskRegister(taskRegister)
    }
    // delete
    suspend fun removeTaskRegister(taskRegister: TaskRegister){
        repository.removeTaskRegister(taskRegister)
    }
    suspend fun removeTaskRegisterList(vararg taskRegisters:TaskRegister){
        repository.removeTaskRegisterList(*taskRegisters)
    }
    suspend fun clearAll(){
        repository.clearAll()
    }
    suspend fun deleteAll(){
        repository.deleteAll()
    }
}

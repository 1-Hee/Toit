package com.one.toit.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.one.toit.data.TaskDataBase
import com.one.toit.data.model.TaskRegister
import com.one.toit.data.repository.TaskRegisterRepository
import timber.log.Timber

class TaskRegisterViewModel(
    private val application: Application
) : AndroidViewModel(application) {
    // db init
    private val repository:TaskRegisterRepository
    init {
        val taskRegistrationDao = TaskDataBase
                                    .getInstance(application.applicationContext)
                                    .TaskRegistrationDao()
        repository = TaskRegisterRepository(taskRegistrationDao)
    }
    // create
    suspend fun addTaskRegister(taskRegister: TaskRegister):Long{
        Timber.d("vm.. addTaskRegister : %s", taskRegister)
       return repository.addTaskRegister(taskRegister)
    }
    suspend fun addAllTaskRegister(vararg taskRegisters : TaskRegister){
        Timber.d("vm.. addAllTaskRegister : %s", taskRegisters)
        repository.addAllTaskRegister(*taskRegisters)
    }
    // read
    suspend fun readTaskRegisterList():LiveData<List<TaskRegister>>{
        Timber.d("vm.. readTaskRegisterList ...")
        return repository.readTaskRegisterList()
    }
    // update
    suspend fun modifyTaskRegister(taskRegister: TaskRegister){
        Timber.d("vm.. modifyTaskRegister : %s", taskRegister)
        repository.modifyTaskRegister(taskRegister)
    }
    // delete
    suspend fun removeTaskRegister(taskRegister: TaskRegister){
        Timber.d("vm.. removeTaskRegister : %s", taskRegister)
        repository.removeTaskRegister(taskRegister)
    }
    suspend fun removeTaskRegisterList(vararg taskRegisters:TaskRegister){
        Timber.d("vm.. removeTaskRegisterList : %s", taskRegisters)
        repository.removeTaskRegisterList(*taskRegisters)
    }
//    suspend fun clearAll(){
//        Timber.d("vm.. clearAll")
//        repository.clearAll()
//    }
    suspend fun deleteAll(){
        Timber.d("vm.. deleteAll")
        repository.deleteAll()
    }
}

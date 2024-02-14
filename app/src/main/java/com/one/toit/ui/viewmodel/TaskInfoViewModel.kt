package com.one.toit.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.one.toit.data.TaskDataBase
import com.one.toit.data.model.TaskInfo
import com.one.toit.data.repository.TaskInfoRepository
import timber.log.Timber

class TaskInfoViewModel (
    private val application: Application
) : AndroidViewModel(application){
    //db init
    private val repository:TaskInfoRepository
    init {
        val taskInfoDao = TaskDataBase
                            .getInstance(application.applicationContext)
                            .TaskInfoDao()
        repository = TaskInfoRepository(taskInfoDao)
    }
    // create
    suspend fun addTaskInfo(taskInfo: TaskInfo):Long{
        Timber.d("vm.. addTaskInfo : %s", taskInfo)
        return repository.addTaskInfo(taskInfo)
    }
    suspend fun addAllTaskInfo(vararg taskInfo: TaskInfo){
        Timber.d("vm.. addAllTaskInfo : %s", taskInfo)
        repository.addAllTaskInfo(*taskInfo)
    }
    // read
    suspend fun readTaskInfoList(): LiveData<List<TaskInfo>> {
        Timber.d("vm.. readTaskInfoList...")
        return repository.readTaskInfoList()
    }
    suspend fun readTaskInfoListWithTaskId(fkTaskId:Long): LiveData<TaskInfo>{
        Timber.d("vm.. readTaskInfoListWithTaskId... %s", fkTaskId)
        return repository.readTaskInfoListWithTaskId(fkTaskId)
    }
    // update
    suspend fun modifyTaskInfo(taskInfo: TaskInfo){
        Timber.d("vm.. modifyTaskInfo : %s", taskInfo)
        repository.modifyTaskInfo(taskInfo)
    }
    // delete
    suspend fun removeTaskInfo(taskInfo: TaskInfo){
        Timber.d("vm.. removeTaskInfo : %s", taskInfo)
        repository.removeTaskInfo(taskInfo)
    }
    suspend fun removeTaskInfoList(vararg taskInfo: TaskInfo){
        Timber.d("vm.. removeTaskInfoList : %s", taskInfo)
        repository.removeTaskInfoList(*taskInfo)
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


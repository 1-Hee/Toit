package com.one.toit.data.repository

import androidx.lifecycle.LiveData
import com.one.toit.data.dao.TaskInfoDao
import com.one.toit.data.model.TaskInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class TaskInfoRepository(
    private val taskInfoDao: TaskInfoDao
) {
    // create
    suspend fun addTaskInfo(taskInfo: TaskInfo):Long
            = withContext(Dispatchers.IO) {
        Timber.d("addTaskInfo.. %s", taskInfo)
        return@withContext taskInfoDao.addTaskInfo(taskInfo)
    }
    suspend fun addAllTaskInfo(vararg taskInfo: TaskInfo)
            = withContext(Dispatchers.IO){
        Timber.d("addAllTaskInfo.. %s", taskInfo)
        taskInfoDao.addAllTaskInfo(*taskInfo)
    }
    // read
    suspend fun readTaskInfoList(): LiveData<List<TaskInfo>>
            = withContext(Dispatchers.IO){
        Timber.d("readTaskInfoList...")
        taskInfoDao.readTaskInfoList()
    }
    suspend fun readTaskInfoListWithTaskId(fkTaskId:Long): LiveData<TaskInfo>
        = withContext(Dispatchers.IO){
        Timber.d("readTaskInfoListWithTaskId... %s", fkTaskId)
        taskInfoDao.readTaskInfoListWithTaskId(fkTaskId)
    }
    // update
    suspend fun modifyTaskInfo(taskInfo: TaskInfo)
            = withContext(Dispatchers.IO){
        Timber.d("modifyTaskInfo... %s", taskInfo)
        taskInfoDao.modifyTaskInfo(taskInfo)
    }
    // delete
    suspend fun removeTaskInfo(taskInfo: TaskInfo)
            = withContext(Dispatchers.IO){
        Timber.d("removeTaskInfo... %s", taskInfo)
        taskInfoDao.removeTaskInfo(taskInfo)
    }

    suspend fun removeTaskInfoList(vararg taskInfo:TaskInfo)
            = withContext(Dispatchers.IO){
        Timber.d("removeTaskInfoList... %s", taskInfo)
        taskInfoDao.removeTaskInfoList(*taskInfo)
    }

//    suspend fun clearAll()
//            = withContext(Dispatchers.IO){
//        Timber.d("clearAll...")
//        taskInfoDao.clearAll()
//    }

    suspend fun deleteAll()
            = withContext(Dispatchers.IO){
        Timber.d("deleteAll...")
        taskInfoDao.deleteAll()
    }
}
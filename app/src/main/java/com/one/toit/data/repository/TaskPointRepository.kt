package com.one.toit.data.repository

import com.one.toit.data.dao.TaskPointDao
import com.one.toit.data.model.TaskPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.Date

class TaskPointRepository(
    private val dao:TaskPointDao
) {
    // C
    suspend fun addTaskPoint(taskPoint: TaskPoint):Long
        = withContext(Dispatchers.IO){
            Timber.d("addTaskPoint.. %s", taskPoint)
        return@withContext dao.addTaskPoint(taskPoint)
    }

    suspend fun addAllTaskPoint(vararg taskPoint: TaskPoint)
        = withContext(Dispatchers.IO) {
            Timber.d("addAllTaskPoint.. %s", taskPoint)
        return@withContext dao.addAllTaskPoint(*taskPoint)
    }

    // R
    suspend fun readTaskPointList():List<TaskPoint>
        = withContext(Dispatchers.IO){
            Timber.d("readTaskPointList... ")
        return@withContext dao.readTaskPointList()
    }

    // U
    suspend fun modifyTaskPoint(taskPoint: TaskPoint)
        = withContext(Dispatchers.IO) {
            Timber.d("modifyTaskPoint... %s", taskPoint)
        return@withContext dao.modifyTaskPoint(taskPoint)
    }

    // D
    suspend fun removeTaskPoint(taskPoint: TaskPoint)
        = withContext(Dispatchers.IO){
            Timber.d("removeTaskPoint... %s", taskPoint)
        return@withContext dao.removeTaskPoint(taskPoint)
    }
    suspend fun removeTaskPointList(vararg taskPoint: TaskPoint)
        = withContext(Dispatchers.IO){
            Timber.d("removeTaskPointList... %s", taskPoint)
        return@withContext dao.removeTaskPointList(*taskPoint)
    }

    // delete all
    suspend fun deleteAll()
        = withContext(Dispatchers.IO){
            Timber.d("delete All...")
        return@withContext dao.deleteAll()
    }

    /**
     *  점수 산정을 위한 메서드
     */
    // 일일 첫 등록인지 판단하는 메서드
    suspend fun checkIsFirst(mDate: Date):Boolean
        = withContext(Dispatchers.IO){
        Timber.d("checkIsFirst... %s", mDate)
        return@withContext dao.checkIsFirst(mDate)
    }

    // 점수 가져오는 메서드!
    suspend fun getToitPoint():Long
        = withContext(Dispatchers.IO) {
        Timber.d("getToitPoint...")
        return@withContext dao.getToitPoint()
    }


}
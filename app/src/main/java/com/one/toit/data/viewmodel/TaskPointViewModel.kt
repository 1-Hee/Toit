package com.one.toit.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.one.toit.data.TaskDataBase
import com.one.toit.data.model.TaskPoint
import com.one.toit.data.repository.TaskPointRepository
import timber.log.Timber
import java.util.Date

class TaskPointViewModel(
    private val application: Application
) : AndroidViewModel(application) {
    // db init
    private val repository:TaskPointRepository
    init {
        val dao = TaskDataBase
            .getInstance(application.applicationContext)
            .TaskPointDao()
        repository = TaskPointRepository(dao)
    }
    // C
    suspend fun addTaskPoint(taskPoint: TaskPoint) : Long {
        Timber.d("vm addTaskPoint... %s", taskPoint)
        return repository.addTaskPoint(taskPoint)
    }
    suspend fun addAllTaskPoint(vararg taskPoint: TaskPoint) {
        Timber.d("vm addAllTaskPoint... %s", taskPoint)
        return repository.addAllTaskPoint(*taskPoint)
    }
    // R
    suspend fun readTaskPointList():List<TaskPoint> {
        Timber.d("vm readTaskPointList...")
        return repository.readTaskPointList()
    }

    // U
    suspend fun modifyTaskPoint(taskPoint: TaskPoint) {
        Timber.d("vm modifyTaskPoint... %s", taskPoint)
        return repository.modifyTaskPoint(taskPoint)
    }

    // D
    suspend fun removeTaskPoint(taskPoint: TaskPoint) {
        Timber.d("vm removeTaskPoint... %s", taskPoint)
        repository.removeTaskPoint(taskPoint)
    }

    suspend fun removeTaskPointList(vararg taskPoint: TaskPoint) {
        Timber.d("vm removeTaskPointList... %s", taskPoint)
        repository.removeTaskPointList(*taskPoint)
    }

    //delete all
    suspend fun deleteAll(){
        Timber.d("vm delete all...")
        repository.deleteAll()
    }

    /**
     *  점수 산정을 위한 메서드
     */
    suspend fun checkIsFirst(mDate: Date):Boolean {
        Timber.d("vm checkIsFirst... %s", mDate)
        return repository.checkIsFirst(mDate)
    }

    // 점수 가져오는 메서드
    suspend fun getToitPoint():Long {
        Timber.d("vm getToitPoint...")
        return repository.getToitPoint()
    }

}
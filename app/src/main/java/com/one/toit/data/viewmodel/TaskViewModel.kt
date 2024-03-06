package com.one.toit.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.one.toit.data.TaskDataBase
import com.one.toit.data.model.Task
import com.one.toit.data.repository.TaskInfoRepository
import com.one.toit.data.repository.TaskRepository
import kotlinx.coroutines.launch
import java.util.Date

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
    // 페이징 추가
    suspend fun readTaskList(page:Int):List<Task>{
        return repository.readTaskList(page)
    }
    // 페이징 & 검색
    suspend fun readTaskListByQuery(page:Int, query:String): List<Task>{
        return repository.readTaskListByQuery(page, query)
    }

    suspend fun getAllTaskCnt(): Int{
        return repository.getAllTaskCnt()
    }

    suspend fun readNotCompleteTaskList():List<Task>{
        return repository.readNotCompleteTaskList()
    }
    // 페이징 추가
    suspend fun readNotCompleteTaskList(page:Int):List<Task>{
        return repository.readNotCompleteTaskList(page)
    }

    suspend fun readTaskListByDate(targetDate: Date): List<Task>{
        return repository.readTaskListByDate(targetDate)
    }
    // 카운트 함수
    suspend fun getTaskCntByDate(targetDate: Date): Int {
        return repository.getTaskCntByDate(targetDate)
    }

    // 페이징 추가
    suspend fun readTaskListByDate(page:Int, targetDate: Date): List<Task>{
        return repository.readTaskListByDate(page, targetDate)
    }

    suspend fun readNotCompleteTaskListByDate(targetDate: Date): List<Task>{
        return repository.readNotCompleteTaskListByDate(targetDate)
    }
    // 페이징 추가
    suspend fun readNotCompleteTaskListByDate(page:Int, targetDate: Date): List<Task>{
        return repository.readNotCompleteTaskListByDate(page, targetDate)
    }

    // 카운트 함수
    suspend fun getNotCompleteTaskCntByDate(targetDate: Date): Int{
        return repository.getNotCompleteTaskCntByDate(targetDate)
    }

    // 통계 관련
    // 일일 전체 Task 개수
    suspend fun getAllTodayTaskCount(targetDate: Date):Int{
        return repository.getAllTodayTaskCount(targetDate)
    }

    // 완료한 전체 Task 개수
    suspend fun getCompleteTodayTaskCount(targetDate: Date):Int {
        return repository.getCompleteTodayTaskCount(targetDate)
    }

}
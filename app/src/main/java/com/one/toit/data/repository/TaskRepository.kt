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
    // 페이징 추가
    suspend fun readTaskList(page:Int):List<Task> = withContext(Dispatchers.IO){
        return@withContext taskDao.readTaskList(page)
    }

    // 페이징 & 검색
    suspend fun readTaskListByQuery(page:Int, query:String): List<Task> = withContext(Dispatchers.IO){
        return@withContext taskDao.readTaskListByQuery(page, query)
    }

    // 카운트 함수
    suspend fun getAllTaskCnt(): Int = withContext(Dispatchers.IO){
        return@withContext taskDao.getAllTaskCnt()
    }

    suspend fun readNotCompleteTaskList():List<Task> = withContext(Dispatchers.IO){
        return@withContext taskDao.readNotCompleteTaskList()
    }
    // 페이징 추가
    suspend fun readNotCompleteTaskList(page:Int):List<Task> = withContext(Dispatchers.IO){
        return@withContext taskDao.readNotCompleteTaskList(page)
    }

    suspend fun readTaskListByDate(targetDate: Date): List<Task> = withContext(Dispatchers.IO){
        return@withContext taskDao.readTaskListByDate(targetDate)
    }
    // 페이징 추가
    suspend fun readTaskListByDate(page:Int, targetDate: Date): List<Task> = withContext(Dispatchers.IO){
        return@withContext taskDao.readTaskListByDate(page, targetDate)
    }

    suspend fun getTaskCntByDate(targetDate: Date): Int = withContext(Dispatchers.IO){
        return@withContext taskDao.getTaskCntByDate(targetDate)
    }
    suspend fun readNotCompleteTaskListByDate(targetDate: Date): List<Task> = withContext(Dispatchers.IO){
        return@withContext taskDao.readNotCompleteTaskListByDate(targetDate)
    }

    // 페이징 추가
    suspend fun readNotCompleteTaskListByDate(page:Int, targetDate: Date): List<Task> = withContext(Dispatchers.IO){
        return@withContext taskDao.readNotCompleteTaskListByDate(page, targetDate)
    }
    suspend fun getNotCompleteTaskCntByDate(targetDate: Date): Int = withContext(Dispatchers.IO){
        return@withContext taskDao.getNotCompleteTaskCntByDate(targetDate)
    }

    // 통계 관련
    // 일일 전체 Task 개수
    suspend fun getAllTodayTaskCount(targetDate: Date):Int = withContext(Dispatchers.IO){
        return@withContext taskDao.getAllTodayTaskCount(targetDate)
    }

    // 완료한 전체 Task 개수
    suspend fun getCompleteTodayTaskCount(targetDate: Date):Int = withContext(Dispatchers.IO){
        return@withContext taskDao.getCompleteTodayTaskCount(targetDate)
    }
}
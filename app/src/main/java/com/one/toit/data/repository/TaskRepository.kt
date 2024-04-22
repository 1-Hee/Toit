package com.one.toit.data.repository

import com.one.toit.data.dao.TaskDao
import com.one.toit.data.dto.TaskCounter
import com.one.toit.data.model.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.Date

class TaskRepository(
    private val taskDao:TaskDao
) {
    suspend fun readTaskList():List<Task>
        = withContext(Dispatchers.IO){
        return@withContext taskDao.readTaskList()
    }
    // 페이징 추가
    suspend fun readTaskList(page:Int):List<Task>
        = withContext(Dispatchers.IO){
        return@withContext taskDao.readTaskList(page)
    }

    // 페이징 & 검색
    suspend fun readTaskListByQuery(page:Int, query:String): List<Task>
        = withContext(Dispatchers.IO){
        return@withContext taskDao.readTaskListByQuery(page, query)
    }

    suspend fun readRemainTaskList():List<Task>
        = withContext(Dispatchers.IO){
        return@withContext taskDao.readRemainTaskList()
    }
    // 페이징 추가
    suspend fun readRemainTaskList(page:Int):List<Task>
        = withContext(Dispatchers.IO){
        return@withContext taskDao.readRemainTaskList(page)
    }

    suspend fun readTaskListByDate(targetDate: Date): List<Task>
        = withContext(Dispatchers.IO){
        return@withContext taskDao.readTaskListByDate(targetDate)
    }
    // 페이징 추가
    suspend fun readTaskListByDate(page:Int, targetDate: Date): List<Task>
        = withContext(Dispatchers.IO){
        return@withContext taskDao.readTaskListByDate(page, targetDate)
    }

    suspend fun readRemainTaskListByDate(targetDate: Date): List<Task>
        = withContext(Dispatchers.IO){
        return@withContext taskDao.readRemainTaskListByDate(targetDate)
    }

    // 페이징 추가
    suspend fun readRemainTaskListByDate(page:Int, targetDate: Date): List<Task>
        = withContext(Dispatchers.IO){
        return@withContext taskDao.readRemainTaskListByDate(page, targetDate)
    }


    // 통계 관련
    /**
     * 주간 통계 관련!
     */
    suspend fun getTaskCntByDate(targetDate: Date): Int
            = withContext(Dispatchers.IO){
        return@withContext taskDao.getTaskCntByDate(targetDate)
    }

    suspend fun getRemainTaskCntByDate(targetDate: Date): Int
            = withContext(Dispatchers.IO){
        return@withContext taskDao.getRemainTaskCntByDate(targetDate)
    }

    // 완료한 전체 Task 개수
    suspend fun getCompleteTaskCnt(targetDate: Date):Int
            = withContext(Dispatchers.IO){
        return@withContext taskDao.getCompleteTaskCnt(targetDate)
    }


    suspend fun getWeeklyCounterList(mDate:Date):List<TaskCounter>
        = withContext(Dispatchers.IO) {
        // taskDao
        var cnt = 0;
        val calendar:Calendar = Calendar.getInstance()
        calendar.time = mDate;
        val mTaskCounterList = mutableListOf<TaskCounter>();
        while(cnt < 7){
            val qDate = calendar.time
            // 일일 task 개수
            val dTotalCnt = taskDao.getTaskCntByDate(qDate)
            // 일일 task 완료 개수
            val dCompleteCnt = taskDao.getCompleteTaskCnt(qDate)
            // dto
            val mTaskCount = TaskCounter(
                totalTask = dTotalCnt,
                completeTask = dCompleteCnt,
                date = qDate
            )
            mTaskCounterList.add(mTaskCount)
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            cnt++;
        }
        return@withContext mTaskCounterList;
    }

    /**
     *  전체 통계 관련
     *  for AllStatisticsPage
     */
    // 전체 목표 수
    suspend fun getAllTaskCnt(): Long
            = withContext(Dispatchers.IO) {
        return@withContext taskDao.getAllTaskCnt()
    }

    // 평균 목표 수
    suspend fun getAvgTaskCnt(): Float
        = withContext(Dispatchers.IO) {
        return@withContext taskDao.getAvgTaskCnt()
    }
    // 월간 목표 수
    suspend fun getMonthTaskCnt(mDate: Date): Long
        = withContext(Dispatchers.IO) {
        return@withContext taskDao.getMonthTaskCnt(mDate)
    }
    // 최장 기록
    suspend fun getMaxTaskTime(): Long
        = withContext(Dispatchers.IO) {
        return@withContext taskDao.getMaxTaskTime()
    }
    // 최단 기록
    suspend fun getMinTaskTime(): Long
        = withContext(Dispatchers.IO) {
        return@withContext taskDao.getMinTaskTime()
    }
    // 평균 기록
    suspend fun getAvgTaskTime(): Float
        = withContext(Dispatchers.IO) {
        return@withContext taskDao.getAvgTaskTime()
    }


}
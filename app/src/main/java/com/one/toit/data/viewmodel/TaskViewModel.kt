package com.one.toit.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.one.toit.data.TaskDataBase
import com.one.toit.data.dto.ChartEntry
import com.one.toit.data.dto.TaskCounter
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

    // 검색
    suspend fun readTaskListByQuery(query:String): List<Task> {
        return repository.readTaskListByQuery(query)
    }

    // 페이징 & 검색
    suspend fun readTaskListByQuery(page:Int, query:String): List<Task>{
        return repository.readTaskListByQuery(page, query)
    }

    suspend fun readRemainTaskList():List<Task>{
        return repository.readRemainTaskList()
    }
    // 페이징 추가
    suspend fun readRemainTaskList(page:Int):List<Task>{
        return repository.readRemainTaskList(page)
    }
    // readNotCompleteTaskList

    suspend fun readTaskListByDate(targetDate: Date): List<Task>{
        return repository.readTaskListByDate(targetDate)
    }
    // 페이징 추가
    suspend fun readTaskListByDate(page:Int, targetDate: Date): List<Task>{
        return repository.readTaskListByDate(page, targetDate)
    }

    suspend fun readRemainTaskListByDate(targetDate: Date): List<Task>{
        return repository.readRemainTaskListByDate(targetDate)
    }
    // 페이징 추가
    suspend fun readRemainTaskListByDate(page:Int, targetDate: Date): List<Task>{
        return repository.readRemainTaskListByDate(page, targetDate)
    }

    /**
     * getTotalTaskCnt
     * getCompleteTaskCnt
     */

    // 통계 관련
    // 일일 전체 Task 개수
    //  getTotalTaskCnt


    /**
     * 주간 통계 관련!
     */

    // 일일 task 개수
    suspend fun getTaskCntByDate(targetDate: Date): Int {
        return repository.getTaskCntByDate(targetDate)
    }

    // 일일 남은 task 개수
    suspend fun getRemainTaskCntByDate(targetDate: Date): Int{
        return repository.getRemainTaskCntByDate(targetDate)
    }

    // 완료한 전체 Task 개수
    suspend fun getCompleteTaskCnt(targetDate: Date):Int {
        return repository.getCompleteTaskCnt(targetDate)
    }

    suspend fun getWeeklyCounterList(mDate:Date):List<TaskCounter> {
        return repository.getWeeklyCounterList(mDate)
                            .sortedBy { it.date }
    }

    /**
     *  전체 통계 관련
     */
    // 전체 목표 수
    suspend fun getAllTaskCnt(): Long{
        return repository.getAllTaskCnt()
    }
    // 평균 목표 수
    suspend fun getAvgTaskCnt(): Float {
        return repository.getAvgTaskCnt()
    }
    // 월간 목표 수
    suspend fun getMonthTaskCnt(mDate: Date): Long {
        return repository.getMonthTaskCnt(mDate)
    }
    // 최장 기록
    suspend fun getMaxTaskTime(): Long {
        return repository.getMaxTaskTime()
    }
    // 최단 기록
    suspend fun getMinTaskTime(): Long {
        return repository.getMinTaskTime()
    }
    // 평균 기록
    suspend fun getAvgTaskTime(): Float {
        return repository.getAvgTaskTime()
    }

    /**
     *  점수 산정을 위한 메서드
     */
    // 점수 구하는 메서드!
    suspend fun getTaskPoint(mDate: Date, fkTaskId: Long):Int {
        return repository.getTaskPoint(mDate, fkTaskId)
    }

    /**
     * 주어진 시간대 까지의 목표 달성 기록을 체크
     */
    suspend fun getAchievementMap(targetDate: Date, totalCnt:Int) : MutableMap<String, ChartEntry>{
        return repository.getAchievementMap(targetDate, totalCnt)
    }
}
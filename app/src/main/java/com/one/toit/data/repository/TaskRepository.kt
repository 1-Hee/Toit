package com.one.toit.data.repository

import com.one.toit.data.dao.TaskDao
import com.one.toit.data.dto.ChartEntry
import com.one.toit.data.dto.TaskCounter
import com.one.toit.data.model.Task
import com.one.toit.ui.compose.style.purple100
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.Calendar
import java.util.Date
import kotlin.math.round

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

    /**
     *  점수 산정을 위한 메서드
     */

    // 메모 점수 산정을 위한 메모 길이
    private suspend fun getMemoLength(fkTaskId:Long):Int
            = withContext(Dispatchers.IO) {
        Timber.d("getMemoLength... %s", fkTaskId)
        return@withContext taskDao.getMemoLength(fkTaskId)
    }

    // 달성 시간 구하는 메서드
    private suspend fun getCompleteTime(fkTaskId: Long):Long
            = withContext(Dispatchers.IO) {
        Timber.d("getCompleteTime.. %s", fkTaskId)
        return@withContext taskDao.getCompleteTime(fkTaskId)
    }

    // 점수 구하는 메서드!
    suspend fun getTaskPoint(mDate: Date, fkTaskId: Long):Int
            = withContext(Dispatchers.IO){
        var mPoint = 0;
        val mLength = getMemoLength(fkTaskId)
        // 메모 계수 적용하여 10자 이상 작성하면 10점
        if(mLength > 10) mPoint += 10

        val completeTime = getCompleteTime(fkTaskId)
        if(completeTime > 0){ // 마감시간 있음
            val time = ( completeTime / 60 ) + 1
            mPoint += 30
            val extraPoint = ( (5 * (time * (time + 1)).toDouble())) / 2
            Timber.d("추가 점수 : %s", extraPoint)
            mPoint += extraPoint.toInt()
        }else { // 마감시간 없음
            mPoint += 30
        }
        return@withContext mPoint
    }

    /**
     * 주어진 시간대 까지의 목표 달성 기록을 체크
     */
    private suspend fun getAchievementCnt(targetDate: Date):Int
        = withContext(Dispatchers.IO){
        Timber.d("getAchievementCnt .. %s", targetDate)
        return@withContext taskDao.getAchievementCnt(targetDate)
    }

    suspend fun getAchievementMap(targetDate: Date, totalCnt:Int) : MutableMap<String, ChartEntry> {
        val calendar = Calendar.getInstance()
        calendar.time = targetDate
        // 00시부터 6시까지인지 확인.
        val gap = 5
        val cHour = calendar.get(Calendar.HOUR_OF_DAY)
        val isDawn =  cHour - gap <= 0;
        val mChartEntryMap = mutableMapOf<String, ChartEntry>()

        if(isDawn){ // 00시부터 현재까지 1시간 단위로 맵핑..!
            for(i in 0..cHour){
                val timeStr = String.format("%02d:00", i)
                calendar.set(Calendar.HOUR_OF_DAY, i);
                val timeCnt = getAchievementCnt(calendar.time)
                val ratio = timeCnt / totalCnt.toFloat()
                val volume = round(ratio * 100).toInt()
                val entry = ChartEntry(volume)
                mChartEntryMap[timeStr] = entry
            }
        }else {
            val mStart = cHour - gap;
            for (i in mStart..cHour) {
                val timeStr = if (i == mStart) {
                    String.format("~%02d:00", i)
                } else {
                    String.format("%02d:00", i)
                }
                calendar.set(Calendar.HOUR_OF_DAY, i);
                val timeCnt = getAchievementCnt(calendar.time)
                val ratio = timeCnt / totalCnt.toFloat()
                val volume = round(ratio * 100).toInt()
                val entry = ChartEntry(volume)
                mChartEntryMap[timeStr] = entry
            }
        }
        return mChartEntryMap
    }

}
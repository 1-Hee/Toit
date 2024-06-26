package com.one.toit.data.repository

import android.content.Context
import com.one.toit.R
import com.one.toit.data.dao.TaskDao
import com.one.toit.data.dto.ChartEntry
import com.one.toit.data.dto.TaskCounter
import com.one.toit.data.model.Task
import com.one.toit.ui.compose.style.purple100
import com.one.toit.util.AppUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.Calendar
import java.util.Date
import kotlin.math.round

class TaskRepository(
    private val taskDao:TaskDao
) {

    private val size = AppUtil.LIST_SIZE;

    // 페이징 + 정렬,
    suspend fun readSortedTaskList(page:Int, orderIdx:Int):List<Task>
        = withContext(Dispatchers.IO){
        return@withContext when(orderIdx){
            1 -> taskDao.readTaskListByName(page, size) // 이름순
            2 -> taskDao.readTaskListByDateDesc(page, size) // 최신순
            3 -> taskDao.readTaskListByDateAsc(page, size) // 과거순
            4 -> taskDao.readTaskListByComplete(page, size) // 목표 상태
            else -> taskDao.readTaskList(page, size) // 선택 안함
        }
    }

    // 페이징 & 검색
//    suspend fun readTaskListByQuery(page:Int, query:String): List<Task>
//        = withContext(Dispatchers.IO){
//        return@withContext taskDao.readTaskListByQuery(page, size, query)
//    }

    // 페이징 & 검색, 정렬 포함
    suspend fun readSortedListWithQuery(
        page:Int, query:String, orderIdx: Int
    ): List<Task> = withContext(Dispatchers.IO){
        return@withContext when(orderIdx){
            1 -> taskDao.readQueryListByName(page, size, query) // 이름순
            2 -> taskDao.readQueryListByDateDesc(page, size, query) // 최신순
            3 -> taskDao.readQueryListByDateAsc(page, size, query) // 과거순
            4 -> taskDao.readQueryListByComplete(page, size, query) // 목표 상태
            else -> taskDao.readQueryList(page, size, query) // 선택 안함
        }
    }


    // 페이징 추가
    suspend fun readTaskListByDate(page:Int, targetDate: Date): List<Task>
        = withContext(Dispatchers.IO){
        return@withContext taskDao.readTaskListByDate(page, size, targetDate)
    }

    suspend fun readRemainTaskListByDate(targetDate: Date): List<Task>
        = withContext(Dispatchers.IO){
        return@withContext taskDao.readRemainTaskListByDate(targetDate)
    }

    // 페이징 추가
    suspend fun readRemainTaskListByDate(page:Int, targetDate: Date): List<Task>
        = withContext(Dispatchers.IO){
        return@withContext taskDao.readRemainTaskListByDate(page, size, targetDate)
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
        mPoint += 30

        if(completeTime > 0){ // 마감시간 있음
//            val time = ( completeTime / 60 ) + 1
//            mPoint += 30
//            val extraPoint = ( (5 * (time * (time + 1)).toDouble())) / 2
//            Timber.d("추가 점수 : %s", extraPoint)
            val extraPoint = (completeTime / 60)
            mPoint += extraPoint.toInt()
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

    suspend fun getAchievementMap(
        context: Context,
        targetDate: Date,
        totalCnt:Int
    ) : MutableMap<String, Number> {
        val calendar = Calendar.getInstance()
        calendar.time = targetDate
        // 00시부터 6시까지인지 확인.
        val gap = 5
        val cHour = calendar.get(Calendar.HOUR_OF_DAY)
        val isDawn =  cHour - gap <= 0;
        val mChartEntryMap = mutableMapOf<String, Number>()

        if(isDawn){ // 00시부터 현재까지 1시간 단위로 맵핑..!
            for(i in 0..cHour){
                val timeStr = String.format(context.getString(R.string.unit_dp_hour), i)
                calendar.set(Calendar.HOUR_OF_DAY, i);
                val timeCnt = getAchievementCnt(calendar.time)
                val ratio = timeCnt / totalCnt.toFloat()
                val volume = round(ratio * 100).toInt()
//                val entry = ChartEntry(volume)
                mChartEntryMap[timeStr] = volume
            }
        }else {
            val mStart = cHour - gap;
            for (i in mStart..cHour) {
                val timeStr = if (i == mStart) {
                    String.format(context.getString(R.string.unit_dp_hour_prev), i)
                } else {
                    String.format(context.getString(R.string.unit_dp_hour), i)
                }
                calendar.set(Calendar.HOUR_OF_DAY, i);
                val timeCnt = getAchievementCnt(calendar.time)
                val ratio = timeCnt / totalCnt.toFloat()
                val volume = round(ratio * 100).toInt()
//                val entry = ChartEntry(volume)
                mChartEntryMap[timeStr] = volume
            }
        }
        return mChartEntryMap
    }

    // 체크
    /**
     *  true : all + date
     *  false : remain + date
     */
   suspend fun hasNextItem(page:Int, targetDate: Date, isAll:Boolean):Boolean
    = withContext(Dispatchers.IO) {
        val list =  if(isAll){
            taskDao.readTaskListByDate(page, size, targetDate)
        }else {
           taskDao.readRemainTaskListByDate(page, size, targetDate)
        }
        return@withContext list.isNotEmpty()
    }

    /**
     * all ,
     * query
     */
    suspend fun hasNextData(page: Int, query: String = ""):Boolean
        = withContext(Dispatchers.IO) {
        val list = if (query.isBlank()) {
            taskDao.readTaskList(page, size);
        } else {
            taskDao.readQueryList(page, size, query)
        }
        return@withContext list.isNotEmpty()
    }
}
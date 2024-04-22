package com.one.toit.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.one.toit.data.model.Task
import java.util.Date

@Dao
interface TaskDao {
    /**
     * 전체 조회
     */
    // TODO 정렬 옵션(생성 날짜), 페이징 추가하기
    @Query("SELECT * FROM table_task_registration " +
            "INNER JOIN table_task_information" +
            " ON task_id = fk_task_id")
    fun readTaskList(): List<Task>
    @Query("SELECT * FROM table_task_registration " +
            "INNER JOIN table_task_information " +
            "ON task_id = fk_task_id "+
            "LIMIT 20 OFFSET (:page-1)*20")
    fun readTaskList(page:Int): List<Task> // 페이징 추가한 메서드

    @Query("SELECT * FROM table_task_registration " +
            "INNER JOIN table_task_information " +
            "ON task_id = fk_task_id " +
            "WHERE task_title LIKE '%'||:query||'%' " +
            "LIMIT 20 OFFSET (:page-1)*20")
    fun readTaskListByQuery(page:Int, query:String): List<Task> // 페이징 & 검색
    @Query("SELECT * FROM table_task_registration " +
            "INNER JOIN table_task_information " +
            "ON task_id = fk_task_id WHERE COALESCE(task_complete, '') = ''")
    fun readRemainTaskList(): List<Task>

    @Query("SELECT * FROM table_task_registration " +
            "INNER JOIN table_task_information " +
            "ON task_id = fk_task_id WHERE COALESCE(task_complete, '') = '' " +
            "LIMIT 20 OFFSET(:page-1)*20")
    fun readRemainTaskList(page:Int): List<Task> // 페이징 추가한 메서드

    /**
     *  일일 조회
     */
    @Query("SELECT * FROM table_task_registration a " +
            "INNER JOIN table_task_information " +
            "ON task_id = fk_task_id " +
            "WHERE DATE(a.create_at) = DATE(:targetDate) ")
    fun readTaskListByDate(targetDate: Date): List<Task>

    @Query("SELECT * FROM table_task_registration a " +
            "INNER JOIN table_task_information " +
            "ON task_id = fk_task_id " +
            "WHERE DATE(a.create_at) = DATE(:targetDate) " +
            "LIMIT 20 OFFSET(:page-1)*20")
    fun readTaskListByDate(page: Int, targetDate: Date): List<Task> // 페이징 추가

    @Query("SELECT * FROM table_task_registration a " +
            "INNER JOIN table_task_information " +
            "ON task_id = fk_task_id " +
            "WHERE COALESCE(task_complete, '') = '' " +
            "AND DATE(a.create_at) = DATE(:targetDate)")
    fun readRemainTaskListByDate(targetDate: Date): List<Task>
    // readRemainTaskList(page:Int)

    @Query("SELECT * FROM table_task_registration a " +
            "INNER JOIN table_task_information " +
            "ON task_id = fk_task_id " +
            "WHERE COALESCE(task_complete, '') = '' " +
            "AND DATE(a.create_at) = DATE(:targetDate)" +
            "LIMIT 20 OFFSET(:page-1)*20")
    fun readRemainTaskListByDate(page:Int, targetDate: Date): List<Task> // 페이징 추가

    // 통계 관련...
    // 일일 전체 Task 개수
    // getCompleteTaskCnt

    /**
     * 주간 통계 관련!
     */
    // 일일 task 개수 카운트 함수
    @Query("SELECT COUNT(ti.info_id) FROM table_task_registration tr " +
            "INNER JOIN table_task_information ti " +
            "ON task_id = fk_task_id " +
            "WHERE DATE(tr.create_at) = DATE(:targetDate) ")
    fun getTaskCntByDate(targetDate: Date): Int

    // 일일 완료 task 개수 카운트 함수
    // 카운트 계수
    @Query("SELECT COUNT(ti.info_id) FROM table_task_registration tr " +
            "INNER JOIN table_task_information ti " +
            "ON task_id = fk_task_id " +
            "WHERE COALESCE(task_complete, '') = '' " +
            "AND DATE(tr.create_at) = DATE(:targetDate) ")
    fun getRemainTaskCntByDate(targetDate: Date): Int

    @Query("SELECT COUNT(task_id) FROM table_task_registration a " +
            "INNER JOIN table_task_information " +
            "ON task_id = fk_task_id " +
            "WHERE COALESCE(task_complete, '') != '' " +
            "AND DATE(a.create_at) = DATE(:targetDate)")
    fun getCompleteTaskCnt(targetDate: Date):Int


    /**
     * 전체 통계 관련
     */

    // 전체 목표 수
    @Query("SELECT COUNT(tr.task_id) FROM table_task_registration tr " +
            "INNER JOIN table_task_information ti " +
            "ON task_id = fk_task_id ")
    fun getAllTaskCnt(): Long

    // 평균 목표 수
    // todo... 고쳐야댐 ㅠㅠ
    @Query("SELECT AVG(cnt) AS avg_count " +
            "FROM ( " +
            "    SELECT tr.create_at as mDate, COUNT(tr.task_id) AS cnt " +
            "    FROM table_task_registration tr " +
            "    INNER JOIN table_task_information ti ON tr.task_id = ti.fk_task_id " +
            "    GROUP BY tr.create_at" +
            " ) AS counts; ")
    fun getAvgTaskCnt(): Float
    // 월간 목표 수
    @Query("SELECT COUNT(tr.task_id) FROM table_task_registration tr " +
            "INNER JOIN table_task_information ti " +
            "ON task_id = fk_task_id " +
            "WHERE strftime('YYYY-MM', tr.create_at) = strftime('YYYY-MM', :mDate)")
    fun getMonthTaskCnt(mDate: Date): Long

    // 최장 기록
    @Query("SELECT CASE " +
            "   WHEN task_complete IS NOT NULL" +
            "       THEN (strftime('%s', task_complete) - strftime('%s', create_at)) " +
            "   ELSE null END " +
            "FROM table_task_registration tr " +
            "INNER JOIN table_task_information ti " +
            "ON task_id = fk_task_id " +
            "ORDER BY 1 DESC LIMIT 1"
    )
    fun getMaxTaskTime(): Long
    // 최단 기록
    @Query("SELECT CASE " +
            "   WHEN task_complete IS NOT NULL" +
            "       THEN (strftime('%s', task_complete) - strftime('%s', create_at)) " +
            "   ELSE null END as task_gap " +
            "FROM table_task_registration tr " +
            "INNER JOIN table_task_information ti " +
            "ON task_id = fk_task_id " +
            "WHERE task_gap > 0 "+
            "ORDER BY 1 ASC LIMIT 1"
    )
    fun getMinTaskTime(): Long
    // 평균 기록
    // todo 0값은 어떻게 할지...
    @Query("SELECT AVG( " +
            " CASE " +
            "   WHEN (strftime('%s', task_complete) - strftime('%s', create_at)) > 0 " +
            "       THEN (strftime('%s', task_complete) - strftime('%s', create_at))" +
            "   ELSE null " +
            " END " +
            " ) " +
            "FROM table_task_registration tr " +
            "INNER JOIN table_task_information ti " +
            "ON task_id = fk_task_id "
    )
    fun getAvgTaskTime():Float


}
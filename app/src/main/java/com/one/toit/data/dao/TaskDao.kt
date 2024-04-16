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

    // 카운트 함수
    @Query("SELECT COUNT(ti.info_id) FROM table_task_registration tr " +
            "INNER JOIN table_task_information ti " +
            "ON task_id = fk_task_id ")
    fun getAllTaskCnt(): Int

    @Query("SELECT * FROM table_task_registration " +
            "INNER JOIN table_task_information " +
            "ON task_id = fk_task_id WHERE COALESCE(task_complete, '') = ''")
    fun readNotCompleteTaskList(): List<Task>

    @Query("SELECT * FROM table_task_registration " +
            "INNER JOIN table_task_information " +
            "ON task_id = fk_task_id WHERE COALESCE(task_complete, '') = '' " +
            "LIMIT 20 OFFSET(:page-1)*20")
    fun readNotCompleteTaskList(page:Int): List<Task> // 페이징 추가한 메서드

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

    // 카운트 함수
    @Query("SELECT COUNT(ti.info_id) FROM table_task_registration tr " +
            "INNER JOIN table_task_information ti " +
            "ON task_id = fk_task_id " +
            "WHERE DATE(tr.create_at) = DATE(:targetDate) ")
    fun getTaskCntByDate(targetDate: Date): Int

    @Query("SELECT * FROM table_task_registration a " +
            "INNER JOIN table_task_information " +
            "ON task_id = fk_task_id " +
            "WHERE COALESCE(task_complete, '') = '' " +
            "AND DATE(a.create_at) = DATE(:targetDate)")
    fun readNotCompleteTaskListByDate(targetDate: Date): List<Task>

    @Query("SELECT * FROM table_task_registration a " +
            "INNER JOIN table_task_information " +
            "ON task_id = fk_task_id " +
            "WHERE COALESCE(task_complete, '') = '' " +
            "AND DATE(a.create_at) = DATE(:targetDate)" +
            "LIMIT 20 OFFSET(:page-1)*20")
    fun readNotCompleteTaskListByDate(page:Int, targetDate: Date): List<Task> // 페이징 추가

    // 카운트 계수
    @Query("SELECT COUNT(ti.info_id) FROM table_task_registration tr " +
            "INNER JOIN table_task_information ti " +
            "ON task_id = fk_task_id " +
            "WHERE COALESCE(task_complete, '') = '' " +
            "AND DATE(tr.create_at) = DATE(:targetDate) ")
    fun getNotCompleteTaskCntByDate(targetDate: Date): Int

    // 통계 관련...
    // 일일 전체 Task 개수
    @Query("SELECT COUNT(task_id) FROM table_task_registration a " +
            "INNER JOIN table_task_information " +
            "ON task_id = fk_task_id " +
            "WHERE DATE(a.create_at) = DATE(:targetDate) ")
    fun getAllTodayTaskCount(targetDate: Date):Int

    // 완료한 전체 Task 개수
    @Query("SELECT COUNT(task_id) FROM table_task_registration a " +
            "INNER JOIN table_task_information " +
            "ON task_id = fk_task_id " +
            "WHERE COALESCE(task_complete, '') != '' " +
            "AND DATE(a.create_at) = DATE(:targetDate)")
    fun getCompleteTodayTaskCount(targetDate: Date):Int

    /**
     * 주간 통계 관련!
     */

    // for 주간 막대 그래프 && 주간 꺾은선 그래프
    @Query("SELECT COALESCE(COUNT(task_id), -1) " +
            "FROM table_task_registration a " +
            "INNER JOIN table_task_information " +
            "ON task_id = fk_task_id " +
            "WHERE DATE(a.create_at) <= DATE(:mDate) " +
            "AND DATE(a.create_at) >= DATE(:mDate, '-7 days') " +
            "GROUP BY DATE(a.create_at)"
    )
    fun getWeeklyTaskCountList(mDate:Date):List<Int>


}
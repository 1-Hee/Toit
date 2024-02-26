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
            "INNER JOIN table_task_information" +
            " ON task_id = fk_task_id WHERE COALESCE(task_complete, '') = ''")
    fun readNotCompleteTaskList(): List<Task>

    /**
     *  일일 조회
     */
    // TODO 정렬 옵션(생성 날짜), 페이징 추가하기
    @Query("SELECT * FROM table_task_registration a " +
            "INNER JOIN table_task_information " +
            "ON task_id = fk_task_id " +
            "WHERE DATE(a.create_at) = DATE(:targetDate) ")
    fun readTaskListByDate(targetDate: Date): List<Task>

    @Query("SELECT * FROM table_task_registration a " +
            "INNER JOIN table_task_information " +
            "ON task_id = fk_task_id " +
            "WHERE COALESCE(task_complete, '') = '' " +
            "AND DATE(a.create_at) = DATE(:targetDate)")
    fun readNotCompleteTaskListByDate(targetDate: Date): List<Task>

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



}
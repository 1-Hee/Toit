package com.one.toit.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.one.toit.data.model.Task

@Dao
interface TaskDao {
    /**
     * 전체 조회
     */
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
    @Query("SELECT * FROM table_task_registration " +
            "INNER JOIN table_task_information" +
            " ON task_id = fk_task_id")
    fun readTodayTaskList(): List<Task>

    @Query("SELECT * FROM table_task_registration " +
            "INNER JOIN table_task_information" +
            " ON task_id = fk_task_id WHERE COALESCE(task_complete, '') = ''")
    fun readTodayNotCompleteTaskList(): List<Task>

}
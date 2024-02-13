package com.one.toit.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.one.toit.data.model.TaskInfo
@Dao
interface TaskInfoDao {
    // create
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addTaskInfo(taskInfo: TaskInfo):Long
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addAllTaskInfo(vararg taskInfo: TaskInfo)
    // read
    @Query("SELECT * FROM table_task_information")
    fun readTaskInfoList(): LiveData<List<TaskInfo>>
    // update
    @Update
    fun modifyTaskInfo(taskInfo: TaskInfo)
    // delete
    @Delete
    fun removeTaskInfo(taskInfo: TaskInfo)
    @Delete
    fun removeTaskInfoList(vararg taskInfo: TaskInfo)
    // delete all
//    @Query("DELETE FROM sqlite_sequence where name='table_task_information'")
//    fun clearAll()
    @Query("DELETE FROM table_task_information")
    fun deleteAll()
}
package com.one.toit.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.one.toit.data.model.TaskRegister

@Dao
interface TaskRegistrationDao {
    // create
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addTaskRegister(taskRegister: TaskRegister):Long
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addAllTaskRegister(vararg taskRegisters: TaskRegister)
    // read
    @Query("SELECT * FROM table_task_registration")
    fun readTaskRegisterList(): LiveData<List<TaskRegister>>
    // update
    @Update
    fun modifyTaskRegister(taskRegister: TaskRegister)
    // delete
    @Delete
    fun removeTaskRegister(taskRegister: TaskRegister)
    @Delete
    fun removeTaskRegisterList(vararg taskRegisters: TaskRegister)
    @Query("DELETE FROM table_task_registration WHERE task_id = :taskId")
    fun removeTaskRegisterById(taskId:Long)

    // delete all
//    @Query("DELETE FROM sqlite_sequence where name='table_task_registration'")
//    fun clearAll()
    @Query("DELETE FROM table_task_registration")
    fun deleteAll()
}
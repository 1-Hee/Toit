package com.one.toit.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.one.toit.data.dao.TaskDao
import com.one.toit.data.dao.TaskInfoDao
import com.one.toit.data.dao.TaskRegistrationDao
import com.one.toit.data.model.TaskInfo
import com.one.toit.data.model.TaskRegister

@Database(entities = [TaskRegister::class, TaskInfo::class], version = 1, exportSchema = false)
abstract class TaskDataBase : RoomDatabase(){
    // dao
    abstract fun TaskInfoDao(): TaskInfoDao
    abstract fun TaskRegistrationDao(): TaskRegistrationDao
    abstract fun TaskDao():TaskDao
    companion object {
        @Volatile
        private var INSTANCE:TaskDataBase? = null
        fun getInstance(context: Context):TaskDataBase{
            // context.deleteDatabase("task_data_base")
            if(INSTANCE==null){
                synchronized(this){
                    val instance = Room
                        .databaseBuilder(
                            context.applicationContext,
                            TaskDataBase::class.java,
                            "task_data_base"
                        ).build()
                    INSTANCE = instance
                    return instance
                }
            }else {
                return INSTANCE!!
            }
        }
    }
}
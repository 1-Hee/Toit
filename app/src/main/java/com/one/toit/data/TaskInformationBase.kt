package com.one.toit.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.one.toit.data.dao.TaskInfoDao
import com.one.toit.data.model.TaskInfo
import com.one.toit.data.model.TaskRegister

/**
 *  Task 상세 정보 저장 데이터 베이스,
 */
@Database(entities = [TaskRegister::class, TaskInfo::class], version = 1, exportSchema = false)
abstract class TaskInformationBase : RoomDatabase(){
    // dao
    abstract fun TaskInfoDao():TaskInfoDao
    companion object {
        @Volatile
        private var INSTANCE:TaskInformationBase? = null
        fun getInstance(context:Context):TaskInformationBase{
            if(INSTANCE==null){
                synchronized(this){
                    val instance = Room
                        .databaseBuilder(
                            context.applicationContext,
                            TaskInformationBase::class.java,
                            "table_task_information"
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
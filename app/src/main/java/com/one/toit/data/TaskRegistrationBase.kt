package com.one.toit.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.one.toit.data.dao.TaskRegistrationDao
import com.one.toit.data.model.TaskRegister

/**
 *  Task 등록 데이터 베이스,
 *  일일 단위로 등록한 한 일을 기록하는 데이터 베이스!
 */
@Database(entities = [TaskRegister::class], version = 1, exportSchema = false)
abstract class TaskRegistrationBase : RoomDatabase(){
    // dao
    abstract fun TaskRegistrationDao():TaskRegistrationDao
    companion object {
        @Volatile
        private var INSTANCE:TaskRegistrationBase? = null
        fun getInstance(context:Context):TaskRegistrationBase {
            if(INSTANCE == null){
                synchronized(this){
                    val instance = Room.databaseBuilder(
                        context.applicationContext,
                        TaskRegistrationBase::class.java,
                        "table_task_registration"
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

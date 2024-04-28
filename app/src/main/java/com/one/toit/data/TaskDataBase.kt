package com.one.toit.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.one.toit.data.dao.CommentDao
import com.one.toit.data.dao.TaskDao
import com.one.toit.data.dao.TaskInfoDao
import com.one.toit.data.dao.TaskRegistrationDao
import com.one.toit.data.dao.TaskPointDao
import com.one.toit.data.model.Comment
import com.one.toit.data.model.TaskInfo
import com.one.toit.data.model.TaskRegister
import com.one.toit.data.model.TaskPoint

@Database(entities = [
    TaskRegister::class, TaskInfo::class, // for 할일 기록!
    TaskPoint::class, Comment::class], // 사용자 점수, 격언 저장 table
    version = 1, exportSchema = false)
@TypeConverters(TaskConverter::class)
abstract class TaskDataBase : RoomDatabase(){
    // dao
    abstract fun TaskInfoDao(): TaskInfoDao
    abstract fun TaskRegistrationDao(): TaskRegistrationDao
    abstract fun TaskDao():TaskDao
    abstract fun TaskPointDao():TaskPointDao
    abstract fun CommentDao():CommentDao

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
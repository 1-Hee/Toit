package com.one.toit.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "table_task_registration")
data class TaskRegister(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "task_id")
    var taskId:Long = 0, // 고유 ID
    @ColumnInfo(name = "create_at")
    var createAt:String = Date().toString() // 첫 등록 일자
)

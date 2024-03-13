package com.one.toit.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "table_task_information",
    foreignKeys = [
        ForeignKey(
            entity = TaskRegister::class,
            parentColumns = ["task_id"],
            childColumns = ["fk_task_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
data class TaskInfo(
    @PrimaryKey (autoGenerate = true)
    @ColumnInfo(name = "info_id")
    var infoId:Long = 0, // 자식 테이블 고유 ID
    @ColumnInfo(name = "fk_task_id")
    var fkTaskId:Long, // fk 고유 ID
    @ColumnInfo(name="task_title")
    var taskTitle:String,
    @ColumnInfo(name="task_memo", defaultValue = "")
    var taskMemo:String = "",
    @ColumnInfo(name = "task_limit")
    var taskLimit:Date? = null, // 마감기한
    @ColumnInfo(name = "task_complete")
    var taskComplete: Date? = null, // 마감 시간
    @ColumnInfo(name="task_certification")
    var taskCertification:String? = null
)

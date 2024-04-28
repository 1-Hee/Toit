package com.one.toit.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "table_task_point",
    foreignKeys = [
    androidx.room.ForeignKey(
        entity = TaskRegister::class,
        parentColumns = ["task_id"],
        childColumns = ["fk_task_id"],
        onDelete = androidx.room.ForeignKey.CASCADE,
        onUpdate = androidx.room.ForeignKey.CASCADE
    )]
)
data class TaskPoint(
    @PrimaryKey(autoGenerate = true)
    var orderIdx:Long = 0,
    @ColumnInfo(name = "fk_task_id")
    var fkTaskId:Long = 0,
    @ColumnInfo(name = "create_at")
    var createAt:Date = Date(),
    @ColumnInfo(name = "point")
    var point:Int = 0
)

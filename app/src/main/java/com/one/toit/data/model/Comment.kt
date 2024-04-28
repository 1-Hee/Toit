package com.one.toit.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

/**
 * 앱 내에서 사용할 격언 관리용 table
 */
@Entity(tableName = "table_comment")
data class Comment(
    @PrimaryKey(autoGenerate = true)
    var orderIdx:Long = 0,
    @ColumnInfo(name = "comment")
    var comment:String,
    @ColumnInfo(name = "create_at")
    var createAt:Date = Date(),
)

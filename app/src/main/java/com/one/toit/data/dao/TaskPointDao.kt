package com.one.toit.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.one.toit.data.model.TaskPoint
import java.util.Date

@Dao
interface TaskPointDao {
    // C
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addTaskPoint(taskPoint: TaskPoint):Long // id return!
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addAllTaskPoint(vararg taskPoint: TaskPoint)
    // R
    @Query("SELECT * FROM table_task_point")
    fun readTaskPointList():List<TaskPoint>

    // U
    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun modifyTaskPoint(taskPoint: TaskPoint)

    // D
    @Delete
    fun removeTaskPoint(taskPoint: TaskPoint)
    @Delete
    fun removeTaskPointList(vararg taskPoint: TaskPoint)

    // delete all
    @Query("DELETE FROM table_task_point")
    fun deleteAll()

    /**
     *  점수 산정을 위한 메서드
     */
    // 일일 첫 등록인지 판단하는 메서드
    @Query(
        "SELECT COUNT(orderIdx) == 0 " +
        "FROM table_task_point " +
        "WHERE DATE(create_at) = DATE(:mDate); "
    )
    fun checkIsFirst(mDate: Date):Boolean

    // 점수 가져오는 메서드!
    @Query("SELECT SUM(point) FROM table_task_point;")
    fun getToitPoint():Long
}
package com.one.toit.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.one.toit.data.model.Comment

@Dao
interface CommentDao {
    // C
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addComment(comment: Comment):Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addAllComment(vararg comment: Comment)

    // R
    @Query("SELECT * FROM table_comment")
    fun readCommentList():List<Comment>

    // U
    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun modifyComment(comment: Comment)

    // D
    @Delete
    fun removeComment(comment: Comment)
    @Delete
    fun removeCommentList(vararg comment: Comment)

    // delete all
    @Query("DELETE FROM table_comment")
    fun deleteAll()
}
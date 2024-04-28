package com.one.toit.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.one.toit.data.TaskDataBase
import com.one.toit.data.model.Comment
import com.one.toit.data.repository.CommentRepository
import timber.log.Timber

class CommentViewModel(
    private val application: Application
) : AndroidViewModel(application) {
    // db init
    private val repository: CommentRepository
    init {
        val dao = TaskDataBase
            .getInstance(application.applicationContext)
            .CommentDao()
        repository = CommentRepository(dao)
    }

    // C
    suspend fun addComment(comment: Comment):Long {
        Timber.d("vm addComment... %s", comment)
        return repository.addComment(comment)
    }

    suspend fun addAllComment(vararg comment: Comment) {
        Timber.d("vm addAllComment... %s", comment)
        repository.addAllComment(*comment)
    }
    // R
    suspend fun readCommentList():List<Comment> {
        Timber.d("vm readCommentList...")
        return repository.readCommentList()
    }

    // U
    suspend fun modifyComment(comment: Comment) {
        Timber.d("vm modifyComment... %s", comment)
        repository.modifyComment(comment)
    }
    // D
    suspend fun removeComment(comment: Comment) {
        Timber.d("vm removeComment... %s", comment)
        repository.removeComment(comment)
    }

    suspend fun removeCommentList(vararg comment: Comment) {
        Timber.d("vm removeCommentList... %s", comment)
        repository.removeCommentList(*comment)
    }

    //delete all
    suspend fun deleteAll(){
        Timber.d("vm delete all...")
        repository.deleteAll()
    }
}
package com.one.toit.data.repository

import com.one.toit.data.dao.CommentDao
import com.one.toit.data.model.Comment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class CommentRepository(
    private val dao:CommentDao
){
    // C
    suspend fun addComment(comment: Comment):Long
        = withContext(Dispatchers.IO){
            Timber.d("addComment... %s", comment)
        return@withContext dao.addComment(comment)
    }
    suspend fun addAllComment(vararg comment: Comment)
        = withContext(Dispatchers.IO){
            Timber.d("addAllComment... %s", comment)
        return@withContext dao.addAllComment(*comment)
    }

    // R
    suspend fun readCommentList():List<Comment>
        = withContext(Dispatchers.IO){
            Timber.d("readCommentList...")
        return@withContext dao.readCommentList()
    }
    // U
    suspend fun modifyComment(comment: Comment)
        = withContext(Dispatchers.IO){
            Timber.d("modifyComment... %s", comment)
        return@withContext dao.modifyComment(comment)
    }
    // D
    suspend fun removeComment(comment: Comment)
        = withContext(Dispatchers.IO){
            Timber.d("removeComment... %s", comment)
        return@withContext dao.removeComment(comment)
    }

    suspend fun removeCommentList(vararg comment: Comment)
        = withContext(Dispatchers.IO) {
            Timber.d("removeCommentList... %s", comment)
        return@withContext dao.removeCommentList(*comment)
    }

    // delete all
    suspend fun deleteAll()
            = withContext(Dispatchers.IO){
        Timber.d("delete All...")
        return@withContext dao.deleteAll()
    }

}
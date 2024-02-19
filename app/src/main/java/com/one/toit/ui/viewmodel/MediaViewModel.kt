package com.one.toit.ui.viewmodel

import android.app.Application
import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.AndroidViewModel
import com.one.toit.data.dto.MediaDTO

class MediaViewModel(
    private val application: Application
) : AndroidViewModel(application) {

    /**
     * 이미지 로드
     */
    private fun loadImage(
        resolver: ContentResolver,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): MutableList<MediaDTO> {
        val mediaDTOList = mutableListOf<MediaDTO>()
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.MIME_TYPE,
            MediaStore.Images.Media.DATE_MODIFIED,
            MediaStore.MediaColumns.SIZE
        )
        val cursor: Cursor = resolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            sortOrder
        )
            ?: return mutableListOf();
        val idColum: Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
        val dpNameColumn: Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
        val mimeColumn: Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.MIME_TYPE)
        val dateColumn: Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_MODIFIED)
        val sizeColumn: Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)

        while (cursor.moveToNext()) {
            val id: Long = cursor.getLong(idColum)
            val dpName: String = cursor.getString(dpNameColumn)
            val mimeType: String = cursor.getString(mimeColumn)
            val contentUri: Uri =
                Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id.toString())
            val date: Long = cursor.getLong(dateColumn)
            val fileSize: Long = cursor.getLong(sizeColumn)
            val mediaDTO = MediaDTO(
                fileName = dpName,
                mimeType = mimeType,
                path = contentUri.toString(),
                lastUpdate = date,
                fileSize = fileSize
            )
            mediaDTOList.add(mediaDTO)
        }
        return mediaDTOList
    }

    private fun getImage(
        resolver: ContentResolver,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): MediaDTO {
        var mMediaDTO = MediaDTO()
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.MIME_TYPE,
            MediaStore.Images.Media.DATE_MODIFIED,
            MediaStore.MediaColumns.SIZE
        )
        val cursor: Cursor = resolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            sortOrder
        )
            ?: return mMediaDTO
        val idColum: Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
        val dpNameColumn: Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
        val mimeColumn: Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.MIME_TYPE)
        val dateColumn: Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_MODIFIED)
        val sizeColumn: Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)

        while (cursor.moveToFirst()) {
            val id: Long = cursor.getLong(idColum)
            val dpName: String = cursor.getString(dpNameColumn)
            val mimeType: String = cursor.getString(mimeColumn)
            val contentUri: Uri =
                Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id.toString())
            val date: Long = cursor.getLong(dateColumn)
            val fileSize: Long = cursor.getLong(sizeColumn)
            val mediaDTO = MediaDTO(
                fileName = dpName,
                mimeType = mimeType,
                path = contentUri.toString(),
                lastUpdate = date,
                fileSize = fileSize
            )
            mMediaDTO = mediaDTO
        }
        return mMediaDTO
    }

    /**
     * 내부저장소에서 검색절을 추가한 이미지 목록 로드
     */
    fun getImagesWithFileName(contentResolver: ContentResolver, fileName: String) {
        val selection = MediaStore.Images.Media.DISPLAY_NAME + " LIKE ?"
        val selectionArgs = arrayOf("%$fileName%")
        val sortOrder = MediaStore.Images.Media._ID + " DESC"; // 정렬 기준, 최신순
        val contentList = loadImage(
            resolver = contentResolver,
            selection = selection,
            selectionArgs = selectionArgs,
            sortOrder = sortOrder
        )
    }

    /**
     * 내부저장소에서 검색절을 추가한 이미지 목록 로드
     */
    fun getImageWithFileName(contentResolver: ContentResolver, fileName: String): MediaDTO {
        val selection = MediaStore.Images.Media.DISPLAY_NAME + " LIKE ? "
        val selectionArgs = arrayOf("%$fileName%")
        val sortOrder = MediaStore.Images.Media._ID + " DESC"; // 정렬 기준, 최신순
        val content = getImage(
            resolver = contentResolver,
            selection = selection,
            selectionArgs = selectionArgs,
            sortOrder = sortOrder
        )
        return content
    }
}
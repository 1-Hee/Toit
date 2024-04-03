package com.one.toit.data.dto

import android.os.Parcel
import android.os.Parcelable
import java.util.Date
data class TaskDTO(
    val taskId: Long = -1,
    val createAt: Date = Date(),
    var taskTitle: String = "",
    var taskMemo: String = "",
    var taskLimit: Date? = null,
    var taskComplete: Date? = null,
    var taskCertification: String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        Date(parcel.readLong()),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readDate(),
        parcel.readDate(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(taskId)
        parcel.writeLong(createAt.time)
        parcel.writeString(taskTitle)
        parcel.writeString(taskMemo)
        parcel.writeDate(taskLimit)
        parcel.writeDate(taskComplete)
        parcel.writeString(taskCertification)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TaskDTO> {
        override fun createFromParcel(parcel: Parcel): TaskDTO {
            return TaskDTO(parcel)
        }

        override fun newArray(size: Int): Array<TaskDTO?> {
            return arrayOfNulls(size)
        }
    }
}


/**
 * 확장 함수를 통해 Nullable한 Date 프로퍼티에 대응
 */
// Date 객체를 Parcel에 쓰는 확장 함수
fun Parcel.writeDate(date: Date?) {
    writeLong(date?.time ?: -1L) // Date 객체가 null이면 -1을 씁니다.
}

// Parcel에서 Date 객체를 읽는 확장 함수
fun Parcel.readDate(): Date? {
    val time = readLong()
    return if (time != -1L) Date(time) else null
}
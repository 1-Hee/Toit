package com.one.toit.data.dto

import android.os.Parcel
import android.os.Parcelable
import java.util.Date

data class TaskDTO(
    val taskId:Long = -1,
    val createAt:String = Date().toString(),
    var taskTitle:String = "",
    var taskMemo:String = "",
    var taskLimit:String? = null,
    var taskComplete:String? = null,
    var taskCertification:String? = null
): Parcelable {
    // Parcel에서 읽을 생성자
    private constructor(parcel: Parcel) : this(
        taskId = parcel.readLong(),
        createAt = parcel.readString()?:"",
        taskTitle = parcel.readString()?:"",
        taskMemo = parcel.readString()?:"",
        taskLimit = parcel.readString()?:"",
        taskComplete = parcel.readString()?:"",
        taskCertification = parcel.readString()?:""
    )
    // Parcelable 인터페이스의 writeToParcel 메서드 구현
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(taskId)
        parcel.writeString(createAt)
        parcel.writeString(taskTitle)
        parcel.writeString(taskMemo)
        parcel.writeString(taskLimit)
        parcel.writeString(taskComplete)
        parcel.writeString(taskCertification)
    }
    // Parcelable.Creator 구현
    companion object CREATOR : Parcelable.Creator<TaskDTO> {
        override fun createFromParcel(parcel: Parcel): TaskDTO {
            return TaskDTO(parcel)
        }
        override fun newArray(size: Int): Array<TaskDTO?> {
            return arrayOfNulls(size)
        }
    }
    override fun describeContents(): Int {
        return 0
    }
}

package com.one.toit.data.dto

import android.os.Parcel
import android.os.Parcelable

// db에도 엔티티로 같이 저장될 수 있도록
data class MediaDTO (
    val savedAddress:String = "",
    val fileName:String = "",
    val mimeType:String = "none",
    var hasError:Boolean = false,
    var isSelected:Boolean = false,
    val lastUpdate: Long = -1, // 최종 수정 날짜
    val fileSize: Long = -1 // 파일의 크기
) : Parcelable {
    // Parcel에서 읽을 생성자
    private constructor(parcel: Parcel) : this(
        fileName = parcel.readString() ?: "",
        savedAddress = parcel.readString() ?: "",
        mimeType = parcel.readString() ?: "",
        hasError = parcel.readInt() == 1, // true if equal to 1
        isSelected = parcel.readInt() == 1, // true if equal to 1
        lastUpdate = parcel.readLong(),
        fileSize = parcel.readLong()
    )

    // Parcelable 인터페이스의 writeToParcel 메서드 구현
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(fileName)
        parcel.writeString(savedAddress)
        parcel.writeString(mimeType)
        parcel.writeInt(if (hasError) 1 else 0) // 1 for true, 0 for false
        parcel.writeInt(if (isSelected) 1 else 0) // 1 for true, 0 for false
        parcel.writeLong(lastUpdate)
        parcel.writeLong(fileSize)
    }

    // Parcelable.Creator 구현
    companion object CREATOR : Parcelable.Creator<MediaDTO> {
        override fun createFromParcel(parcel: Parcel): MediaDTO {
            return MediaDTO(parcel)
        }

        override fun newArray(size: Int): Array<MediaDTO?> {
            return arrayOfNulls(size)
        }
    }
    override fun describeContents(): Int {
        return 0
    }
}
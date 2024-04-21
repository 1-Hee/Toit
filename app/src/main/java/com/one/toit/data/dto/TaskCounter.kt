package com.one.toit.data.dto

import java.util.Date

/**
 * 일일 할일 목록  개수, 일일 달성 목표 개수, 일자를 담을 DTO
 */
data class TaskCounter(
    val totalTask:Int = 0,
    val completeTask:Int = 0,
    val date: Date,
)

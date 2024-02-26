package com.one.toit.data.dto

import androidx.compose.ui.graphics.Color
import com.one.toit.ui.compose.style.purple200

data class ChartEntry(
    val volume:Int, // 통계 값, 수치
    val color: Color = purple200 // 그래프에 표시될 색상
)

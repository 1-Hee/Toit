package com.one.toit.ui.compose.ui.unit

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class PackedPieChartEntry(
    val color: Color, val percentage: Float
)

private fun calculateStartAngles(entries: List<PackedPieChartEntry>): List<Float> {
    var totalPercentage = 0f
    return entries.map { entry ->
        val startAngle = totalPercentage * 360
        totalPercentage += entry.percentage
        startAngle
    }
}

@Composable
fun PackedPieChart(entries: List<PackedPieChartEntry>, size: Dp = 300.dp) {
    Canvas(modifier = Modifier.size(size)) {
        val startAngles = calculateStartAngles(entries)
        entries.forEachIndexed { index, entry ->
            drawArc(
                color = entry.color,
                startAngle = startAngles[index],
                sweepAngle = entry.percentage * 360f,
                useCenter = true,
                topLeft = Offset.Zero,
                size = this.size
            )
        }
    }
}
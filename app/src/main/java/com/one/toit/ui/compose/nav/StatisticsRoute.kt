package com.one.toit.ui.compose.nav

import com.one.toit.R

sealed class StatisticsRoute(
    val title: Int, val route:String
){
    object Daily:StatisticsRoute(title = R.string.title_dilay_outline, route = "DailyPage")
    object Weekly:StatisticsRoute(title = R.string.title_weekly, route = "WeeklyPage")
    object Total:StatisticsRoute(title = R.string.title_statistics_all, route = "TotalPage")
}

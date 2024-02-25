package com.one.toit.ui.compose.nav

import com.one.toit.R

sealed class StatisticsRoute(
    val title: Int, val route:String
){
    object DailyOutline:StatisticsRoute(title = R.string.title_dilay_outline, route = "DailyOutlinePage")
    object WeeklyPage:StatisticsRoute(title = R.string.title_weekly, route = "WeeklyPage")
    object AllStatisticsPage:StatisticsRoute(title = R.string.title_statistics_all, route = "AllStatisticsPage")
}

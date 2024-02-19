package com.one.toit.ui.compose.nav

import com.one.toit.R

sealed class StatisticsRoute(
    val title: Int, val route:String
){
    object Weekly:StatisticsRoute(title = R.string.p_app_statistics, route = "WeeklyPage")
}

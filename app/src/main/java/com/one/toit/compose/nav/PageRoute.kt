package com.one.toit.compose.nav

import com.one.toit.R

sealed class PageRoute(
    val title: Int, val icon: Int = -1, val route:String
){
    object Todo: PageRoute(title = R.string.p_todo, icon = R.drawable.ic_todo, route = "TodoPage")
    object Statistics: PageRoute(title = R.string.p_statistics, icon = R.drawable.ic_statistics, route = "StatisticsPage")
    object Profile: PageRoute(title = R.string.p_profile, icon = R.drawable.ic_profile, route = "ProfilePage")
}

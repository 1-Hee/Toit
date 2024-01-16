package com.one.toit.compose.nav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.List
import androidx.compose.material.icons.rounded.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.one.toit.R

sealed class BoardRoute(
    val title: Int, val icon: ImageVector? = null, val route:String
){
    object Todo: BoardRoute(title = R.string.p_todo, icon = Icons.Rounded.List, route = "TodoPage")
    object Statistics: BoardRoute(title = R.string.p_statistics, icon = Icons.Rounded.Favorite, route = "StatisticsPage")
    object Profile: BoardRoute(title = R.string.p_profile, icon = Icons.Rounded.Person, route = "ProfilePage")
}

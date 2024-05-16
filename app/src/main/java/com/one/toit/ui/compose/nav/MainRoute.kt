package com.one.toit.ui.compose.nav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.List
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.ui.graphics.vector.ImageVector
import com.one.toit.R

sealed class MainRoute(
    val title: Int, val route:String
){
    object Todo: MainRoute(title = R.string.p_todo, route = "TodoPage")
    object TodoStatus: MainRoute(title = R.string.p_statistics, route = "TodoStatusPage")
    object Profile: MainRoute(title = R.string.p_profile, route = "ProfilePage")
}

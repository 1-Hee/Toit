package com.one.toit.ui.compose.nav

import com.one.toit.R

sealed class SettingRoute(
    val title: Int, val route:String
) {
    object AppSetting:SettingRoute(title = R.string.p_app_setting, route = "AppSettingPage")

}
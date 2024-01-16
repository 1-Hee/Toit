package com.one.toit.compose.nav

import androidx.compose.ui.res.stringResource
import com.one.toit.R

sealed class SettingRoute(
    val title: Int, val route:String
) {
    object AppSetting:SettingRoute(title = R.string.p_app_setting, route = "AppSettingPage")

}
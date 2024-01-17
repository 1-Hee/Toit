package com.one.toit.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.one.toit.R
import com.one.toit.compose.nav.SettingRoute
import com.one.toit.compose.style.MyApplicationTheme
import com.one.toit.compose.style.white
import com.one.toit.compose.ui.page.AppSettingPage

class SettingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SettingScreenView(this)
        }
    }
}

@Composable
fun SettingScreenView(activity: SettingActivity){
    val navController = rememberNavController()
    Scaffold(
        topBar = { SettingTopBarComponent(activity) },
        bottomBar = {  }
    ) {
        Box(Modifier.padding(it)) {
            SettingNavGraph(navController = navController)
        }
    }
}

@Composable
fun SettingTopBarComponent(activity: SettingActivity){
    Row(modifier = Modifier
        .fillMaxWidth()
        .height(48.dp)
        .background(white)
        .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(32.dp)
    ){
        Icon(
            Icons.Rounded.KeyboardArrowLeft,
            contentDescription = "icBack",
            modifier = Modifier.size(18.dp)
                .clickable {
                    activity.finish()
                }
        )
        val settingTabName = stringResource(id = R.string.p_app_setting)
        Text(
            text = settingTabName,
            style = MaterialTheme.typography.subtitle1
                .copy(
                    fontSize = 16.sp
                ),
            modifier = Modifier.wrapContentSize()
        )
    }
}

@Composable
fun SettingNavGraph(navController: NavHostController){
    NavHost(navController = navController, startDestination = SettingRoute.AppSetting.route){
        composable(SettingRoute.AppSetting.route){
            AppSettingPage()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreView(){
    MyApplicationTheme {
        SettingScreenView(SettingActivity())
    }
}
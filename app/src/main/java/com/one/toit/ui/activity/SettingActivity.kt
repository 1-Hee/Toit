package com.one.toit.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.one.toit.compose.nav.SettingRoute
import com.one.toit.compose.style.MyApplicationTheme
import com.one.toit.compose.style.white
import com.one.toit.compose.ui.page.AppSettingPage

class SettingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SettingScreenView()
        }
    }
}

@Composable
fun SettingScreenView(){
    val navController = rememberNavController()
    Scaffold(
        topBar = { SettingTopBarComponent() },
        bottomBar = {  }
    ) {
        Box(Modifier.padding(it)) {
            SettingNavGraph(navController = navController)
        }
    }
}

@Composable
fun SettingTopBarComponent(){
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(48.dp)
        .background(white)
        .padding(horizontal = 16.dp, vertical = 12.dp)
    ){
        Text(
            text = "제목",
            style = MaterialTheme.typography.subtitle1
                .copy(
                    fontSize = 16.sp
                ),
            modifier = Modifier
                .align(Alignment.CenterStart)
        )

//        // 아이콘
//        Icon(
//            painter = painterResource(id = R.drawable.ic_setting),
//            contentDescription = "환경 설정",
//            modifier = Modifier
//                .width(24.dp)
//                .height(24.dp)
//                .align(Alignment.CenterEnd)
//        )

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
        BoardScreenView()
    }
}
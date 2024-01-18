package com.one.toit.ui.activity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.one.toit.R
import com.one.toit.base.ui.BaseComposeActivity
import com.one.toit.compose.nav.BoardRoute
import com.one.toit.compose.style.MyApplicationTheme
import com.one.toit.compose.ui.page.BoardWritePage
import com.one.toit.compose.ui.unit.SimpleTopBar

class BoardActivity : BaseComposeActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BoardScreenView(this)
        }
    }
}

@Composable
fun BoardScreenView(activity: BoardActivity){
    val navController = rememberNavController()
    Scaffold(
        topBar = { BoardTopBar(activity) },
        bottomBar = {  }
    ) {
        Box(Modifier.padding(it)) {
            BoardNavGraph(navController = navController)
        }
    }
}

@Composable
fun BoardTopBar(activity: BoardActivity){
    val tabName = stringResource(id = R.string.p_board_write) // TODO change to VM
    SimpleTopBar(activity = activity, title = tabName)
}


@Composable
fun BoardNavGraph(navController: NavHostController){
    NavHost(navController = navController, startDestination = BoardRoute.BoardWrite.route){
        composable(BoardRoute.BoardWrite.route){
            BoardWritePage()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BoardPreView(){
    MyApplicationTheme {
        BoardScreenView(BoardActivity())
    }
}
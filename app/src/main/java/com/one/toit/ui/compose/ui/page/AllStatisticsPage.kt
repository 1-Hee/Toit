package com.one.toit.ui.compose.ui.page

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.one.toit.data.viewmodel.TaskViewModel
import com.one.toit.ui.compose.style.black
import com.one.toit.ui.compose.style.white

@Composable
fun AllStatisticsPage(
    navController: NavHostController,
    taskViewModel: TaskViewModel,
    launcher: ActivityResultLauncher<Intent>? = null
){

    /// scroll state
    val outerScrollState = rememberScrollState()

    // dummy!
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(outerScrollState)
            .background(white)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(
            text = "전체 통계...",
            style = MaterialTheme.typography.caption.copy(
                color = black,
                fontSize = 16.sp
            ),
            textAlign = TextAlign.Center,
        )
    }
}
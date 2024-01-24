package com.one.toit.ui.compose.ui.page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.one.toit.R
import com.one.toit.ui.compose.style.black
import com.one.toit.ui.compose.style.white
import com.one.toit.ui.compose.ui.unit.BarGraphChart
import com.one.toit.ui.compose.ui.unit.ChartBar

// @Preview(showBackground = true)
@Composable
fun GraphPage(
    navController: NavHostController
){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(white)
    ) {
//        Text(
//            text = stringResource(id = R.string.p_graph),
//            style = MaterialTheme.typography.h1,
//            textAlign = TextAlign.Center,
//            color = black,
//            modifier = Modifier.align(Alignment.Center)
//        )

        val brush = Brush.horizontalGradient(listOf(Color.Red, Color.Blue))
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            //BarGraphChart()

            BarGraphChart(

            )
        }
    }
}
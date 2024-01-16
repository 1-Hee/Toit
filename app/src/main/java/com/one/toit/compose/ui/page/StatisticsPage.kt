package com.one.toit.compose.ui.page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.one.toit.R
import com.one.toit.compose.style.white

@Preview(showBackground = true)
@Composable
fun StatisticsPage(){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(white)
    ) {
        Text(
            text = stringResource(id = R.string.p_statistics),
            style = MaterialTheme.typography.h1,
            textAlign = TextAlign.Center,
            color = Color.White,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}
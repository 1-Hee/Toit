package com.one.toit.ui.compose.ui.unit.todo

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.one.toit.R
import com.one.toit.ui.compose.style.mono900

@Preview(showBackground = true)
@Composable
fun ItemNoContent(){
    Box(
        modifier = Modifier
            .fillMaxSize()
    ){
        Column(
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.Center),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_no_content),
                contentDescription = "",
                modifier = Modifier
                    .size(128.dp)
                    //.wrapContentSize()
            )
            Text(
                text = stringResource(R.string.txt_no_todo_line1),
                style = MaterialTheme.typography.subtitle1
                    .copy(
                        fontSize = 16.sp,
                        color = mono900
                    )
            )
            Text(
                text = stringResource(R.string.txt_no_todo_line2),
                style = MaterialTheme.typography.subtitle1
                    .copy(
                        fontSize = 16.sp,
                        color = mono900
                    )
            )
        }
    }
}
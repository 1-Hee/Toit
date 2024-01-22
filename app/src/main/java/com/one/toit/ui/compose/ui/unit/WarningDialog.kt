package com.one.toit.ui.compose.ui.unit

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.one.toit.ui.compose.style.black
import com.one.toit.ui.compose.style.mono200
import com.one.toit.ui.compose.style.mono800
import com.one.toit.ui.compose.style.mono900
import com.one.toit.ui.compose.style.red300
import com.one.toit.ui.compose.style.white

@Composable
fun WarningDialog(
    onDismiss: () -> Unit,
    onCancel: () -> Unit,
    onAction: () -> Unit,
    title:String,
    content:String,
    textAction:String = "확인",
    textCancel:String = "취소"
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.subtitle1.copy(
                    fontSize = 16.sp,
                    color = black,
                    textAlign = TextAlign.Start
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            )
        },
        text = {
            Text(
                text = content,
                style = MaterialTheme.typography.subtitle1.copy(
                    fontSize = 14.sp,
                    color = mono800,
                    textAlign = TextAlign.Start
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            )
        },
        confirmButton = {
            Button(onClick = {
                onAction()
                onDismiss()
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = white,
                    contentColor = black,
                    disabledBackgroundColor = mono200,
                    disabledContentColor = black
                ),
                elevation = null
            ) {
                Text(
                    text = textAction,
                    style = MaterialTheme.typography.subtitle1.copy(
                        fontSize = 16.sp,
                        color = red300
                    ),
                    modifier = Modifier
                        .wrapContentSize()
                )

            }
        },
        dismissButton = {
            Button(onClick = {
                onCancel()
                onDismiss()
            },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = white,
                    contentColor = black,
                    disabledBackgroundColor = mono200,
                    disabledContentColor = black
                ),
                elevation = null
            ) {
                Text(
                    text = textCancel,
                    style = MaterialTheme.typography.subtitle1.copy(
                        fontSize = 16.sp,
                        color = black
                    ),
                    modifier = Modifier
                        .wrapContentSize()
                )
            }
        },
        modifier = Modifier
            .wrapContentWidth()
            .wrapContentHeight()
            .padding(4.dp)
            .clip(RoundedCornerShape(16.dp))
    )
}


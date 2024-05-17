package com.one.toit.ui.compose.ui.unit.statistics

import android.content.Context
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.one.toit.R
import com.one.toit.data.dto.TaskDTO
//import com.one.toit.data.dto.TaskDTO
import com.one.toit.ui.compose.style.black
import com.one.toit.ui.compose.style.mono300
import com.one.toit.ui.compose.style.mono900
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * 정렬 선택상자 컴포저블
 */
@Composable
fun SortUnit(
    context: Context,
    onSort : (index:Int) -> Unit
){
    var expanded by remember { mutableStateOf(false) }
    val options = context.resources.getStringArray(R.array.option_todo_sort)
    var selectedOption by remember { mutableStateOf(options[0]) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.End,
    ){
        Row(
            modifier = Modifier
                .wrapContentSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ){
            Text(
                text = stringResource(R.string.txt_sort_standard),
                style = MaterialTheme.typography.caption.copy(
                    color = black,
                    fontSize = 14.sp,
                ),
            )
            Row(
                modifier = Modifier
                    .width(128.dp)
                    .height(32.dp)
                    .padding(horizontal = 12.dp)
                    .border(
                        width = 1.dp,
                        color = mono300,
                        shape = getBottomLineShape(1.dp)
                    )
                    .clickable {
                        expanded = true
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Text(
                    text = selectedOption,
                    style = MaterialTheme.typography.caption.copy(
                        color = mono900,
                        fontSize = 14.sp,
                    ),
                )
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                )
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            options.forEachIndexed { index, option ->
                DropdownMenuItem(
                    onClick = {
                        selectedOption = option
                        expanded = false
                        onSort(index)
                    },
                ) {
                    Text(
                        text = option,
                    )
                }
            }
        }
    }
}

// 바닥 선만 그려주는 메서드
@Composable
private fun getBottomLineShape(lineThicknessDp: Dp) : Shape {
    val lineThicknessPx = with(LocalDensity.current) {lineThicknessDp.toPx()}
    return GenericShape { size, _ ->
        // 1) Bottom-left corner
        moveTo(0f, size.height)
        // 2) Bottom-right corner
        lineTo(size.width, size.height)
        // 3) Top-right corner
        lineTo(size.width, size.height - lineThicknessPx)
        // 4) Top-left corner
        lineTo(0f, size.height - lineThicknessPx)
    }
}
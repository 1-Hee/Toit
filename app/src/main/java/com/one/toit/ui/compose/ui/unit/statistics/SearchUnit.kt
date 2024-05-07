package com.one.toit.ui.compose.ui.unit.statistics

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.one.toit.R
import com.one.toit.ui.compose.style.black
import com.one.toit.ui.compose.style.mono200
import com.one.toit.ui.compose.style.mono300
import com.one.toit.ui.compose.style.mono400
import com.one.toit.ui.compose.style.mono50
import com.one.toit.ui.compose.style.mono600
import com.one.toit.ui.compose.style.mono900
import com.one.toit.ui.compose.style.none
import com.one.toit.ui.compose.style.purple200

/**
 * 검색창 컴포저블
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchUnit(
    onSearch: (String) -> Unit,
    onDelete: () -> Unit
){
    var searchKeyWord by remember { mutableStateOf("") }
    val searchHint = stringResource(R.string.txt_hint_search_todo)
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    Spacer(modifier = Modifier.height(24.dp))
    Box(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .background(color = mono50, RoundedCornerShape(8.dp))
        .padding(horizontal = 12.dp)
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterStart),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                Icons.Rounded.Search,
                contentDescription = "",
                modifier = Modifier
                    .width(24.dp)
                    .height(24.dp),
                tint = mono300
            )
            OutlinedTextField(
                value = searchKeyWord,
                onValueChange = { keyword ->
                    searchKeyWord = keyword
                    if(keyword.isBlank()){ onDelete() } // 글자가 비었다면 초기화 호출
                },
                keyboardActions = KeyboardActions(onDone = {
                    onSearch(searchKeyWord)
                    focusManager.clearFocus()
                }),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done, keyboardType = KeyboardType.Password
                ),
                maxLines = 1,
                placeholder = {
                    Text(
                        text = searchHint,
                        style = MaterialTheme.typography.subtitle1
                            .copy(
                                fontSize = 14.sp,
                                color = mono200
                            ),
                    )
                },
                colors = TextFieldDefaults.textFieldColors(
                    textColor = mono900,
                    disabledTextColor = Color.Transparent,
                    backgroundColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                )
            );
        }

        // 닫힘 버튼
        if(searchKeyWord.isNotBlank()){
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .border(1.dp, mono600, shape = RoundedCornerShape(8.dp))
                    .background(mono50, RoundedCornerShape(8.dp))
                    .align(Alignment.CenterEnd)
                    .clickable {
                        searchKeyWord = ""
                        keyboardController?.hide()
                        focusManager.clearFocus()
                        onDelete();
                    }
            ){
                Icon(
                    Icons.Rounded.Clear,
                    contentDescription = "",
                    modifier = Modifier
                        .width(12.dp)
                        .height(12.dp)
                        .align(Alignment.Center),
                    tint = mono600
                )
            }
        }
    }
}
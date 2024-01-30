package com.one.toit.ui.compose.ui.unit.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.ModifierLocalModifierNode
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.one.toit.R
import com.one.toit.ui.compose.style.black
import com.one.toit.ui.compose.style.mono200
import com.one.toit.ui.compose.style.mono400
import com.one.toit.ui.compose.style.mono600
import com.one.toit.ui.compose.style.mono700
import com.one.toit.ui.compose.style.purple200
import com.one.toit.ui.compose.style.purple300
import com.one.toit.ui.compose.style.purple400
import com.one.toit.ui.compose.style.red300
import com.one.toit.ui.compose.style.white
import com.one.toit.util.AppUtil
import com.one.toit.util.PreferenceUtil

@Composable
fun EditNickNameDialog(
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val prefs = PreferenceUtil.getInstance(context)
    val nickNameKey = stringResource(id = R.string.key_nickname)
    var mNickName by remember { mutableStateOf(prefs.getValue(nickNameKey)) }
    val inValidComment = stringResource(id = R.string.ts_invalid_nickname)
    val modifiedComment = stringResource(id = R.string.ts_edit_nickname)

    Dialog(
        onDismissRequest = onDismiss
    ) {
        Column(
            Modifier
                .width(256.dp)
                .wrapContentHeight()
                .background(color = white, shape = RoundedCornerShape(12.dp))
            ,
            verticalArrangement = Arrangement.Top
        ) {
            // 상단 탑바
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .background(
                        color = purple200,
                        shape = RoundedCornerShape(
                            topStart = 12.dp,
                            topEnd = 12.dp
                        )
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(id = R.string.txt_h_edit_nickname),
                    fontSize = 14.sp,
                    style = MaterialTheme.typography.caption.copy(
                        color = white
                    )
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)){
                    Text(
                        text = stringResource(id = R.string.txt_guide_nickname),
                        fontSize = 14.sp,
                        style = MaterialTheme.typography.caption.copy(
                            color = mono700
                        ),
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                Row (
                    Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    TextField(
                        value = mNickName,
                        onValueChange = { mNickName = it },
                        placeholder = {stringResource(id = R.string.txt_holder_nickname)},
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(vertical = 8.dp, horizontal = 12.dp),
                        label = {"nickName"},
                        maxLines = 16,
                        singleLine = true,
                        colors = TextFieldDefaults.textFieldColors(
                            textColor = black,
                            disabledTextColor = mono600,
                            backgroundColor = white,
                            cursorColor = purple300,
                            errorCursorColor = red300,
                            focusedIndicatorColor = purple300,
                            placeholderColor = mono400
                        )
                    )
                }
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = stringResource(id = R.string.txt_cancel),
                        fontSize = 14.sp,
                        style = MaterialTheme.typography.caption.copy(
                            color = mono700
                        ),
                        modifier = Modifier
                            .clickable { onDismiss() }
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = stringResource(id = R.string.txt_edit),
                        fontSize = 14.sp,
                        style = MaterialTheme.typography.caption.copy(
                            color = purple400
                        ),
                        modifier = Modifier.clickable {
                            if(mNickName.isBlank()){// 유효하지 않을 경우 return
                                AppUtil.toast(context, inValidComment)
                                return@clickable
                            }
                            prefs.setValue(nickNameKey, mNickName)
                            AppUtil.toast(context, modifiedComment)
                            onDismiss()
                        }
                    )
                }
            }
        }
    }
}
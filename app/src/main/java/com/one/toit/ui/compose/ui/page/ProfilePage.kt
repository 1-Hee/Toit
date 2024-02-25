package com.one.toit.ui.compose.ui.page

import android.app.Activity
import android.content.Intent
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.ContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.material.Text
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.one.toit.R
import com.one.toit.ui.compose.style.black
import com.one.toit.ui.compose.style.green300
import com.one.toit.ui.compose.style.mono100
import com.one.toit.ui.compose.style.mono300
import com.one.toit.ui.compose.style.mono800
import com.one.toit.ui.compose.style.mono900
import com.one.toit.ui.compose.style.navy300
import com.one.toit.ui.compose.style.none
import com.one.toit.ui.compose.style.orange200
import com.one.toit.ui.compose.style.orange300
import com.one.toit.ui.compose.style.purple200
import com.one.toit.ui.compose.style.purple300
import com.one.toit.ui.compose.style.purple400
import com.one.toit.ui.compose.style.purple500
import com.one.toit.ui.compose.style.red100
import com.one.toit.ui.compose.style.white
import com.one.toit.ui.compose.ui.unit.AdmobBanner
import com.one.toit.ui.compose.ui.unit.profile.EditNickNameDialog
import com.one.toit.ui.compose.ui.unit.profile.ProfileMenuDialog
import com.one.toit.ui.compose.ui.unit.profile.ProfilePreviewDialog
import com.one.toit.util.AppUtil
import com.one.toit.util.PreferenceUtil
import com.skydoves.landscapist.CircularReveal
import com.skydoves.landscapist.glide.GlideImage
import timber.log.Timber
import java.text.NumberFormat

@Preview(showBackground = true)
@Composable
fun ProfilePage() {
    Timber.plant(Timber.DebugTree())
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val prefs = PreferenceUtil.getInstance(context)
    val profileImgKey = stringResource(id = R.string.key_profile_img)
    /**
     * 프로필 정보 관련
     */
    val nickNameKey = stringResource(id = R.string.key_nickname)
    var userNickname by remember { mutableStateOf(prefs.getValue(nickNameKey)) } // 사용자 닉네임
    var userProfile by remember { mutableStateOf(prefs.getValue(profileImgKey)) } // 사용자 프로필 이미지 주소
    /**
     * 사용자 프로필 통계 정보
     */
    var todoState by remember { mutableStateOf(5) }
    var todoGoal by remember { mutableStateOf(10) }
    var toitPoint by remember { mutableStateOf(123456) }
    var dayCnt by remember { mutableStateOf(123) }

    // 다이얼로그 창 상태관리 변수
    var showMenuProfile by remember { mutableStateOf(false) }
    var showEditNickName by remember { mutableStateOf(false) }
    val pickSuccess = stringResource(id = R.string.ts_picked_img)
    val pickError = stringResource(id = R.string.ts_error_pick_img)
    // 사진 불러오기 기능
    val photoIntent =
        // ACTION_GET_CONTENT
        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
            type = "image/*"
            action = Intent.ACTION_PICK
            putExtra(
                Intent.EXTRA_MIME_TYPES,
                arrayOf("image/jpeg", "image/png", "image/bmp", "image/webp")
            )
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
        }
    val photoLauncher = // 갤러리에서 사진 가져오기
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    AppUtil.toast(context, pickSuccess)
                    prefs.setValue(profileImgKey, uri.toString())
                    userProfile = uri.toString()
                } ?: run { AppUtil.toast(context, pickError) }
            } else if (result.resultCode != Activity.RESULT_CANCELED) {
                // ??
            }
        }

    if(showMenuProfile){
        ProfileMenuDialog(
            onDismiss = { showMenuProfile = false },
            onNickNameEdit = {
                showMenuProfile = false
                showEditNickName = true
             },
            onEditProfile = {
                showMenuProfile = false
                photoLauncher.launch(photoIntent)
            },
            onInitProfile = {
                prefs.setValue(profileImgKey, "")
                userProfile = ""
                showMenuProfile = false
            }
        )
    }
    if(showEditNickName){
        EditNickNameDialog {
            showEditNickName = false
            userNickname = prefs.getValue(nickNameKey)
        }
    }

    // 프로필 프리뷰
    var isShowProfilePreview by remember { mutableStateOf(false) }
    if(isShowProfilePreview){
        ProfilePreviewDialog(userProfile){
            isShowProfilePreview = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(white)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 사용자 프로필 부분
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ){
                // 프로필 이미지
                Box(modifier = Modifier
                    .size(64.dp)
                    .clip(shape = RoundedCornerShape(32.dp))
                ){
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .background(color = mono100)
                    ){
                        GlideImage(
                            imageModel = userProfile,
                            // contentScale 종류 : Crop, Fit, Inside, FillHeight, FillWidth, None
                            contentScale = ContentScale.Crop,
                            circularReveal = CircularReveal(duration = 0),
                            // 이미지 로딩 전 표시할 place holder 이미지
                            placeHolder = painterResource(id = R.drawable.ic_profile),
                            // 에러 발생 시 표시할 이미지
                            error = painterResource(id = R.drawable.ic_profile),
                            modifier = Modifier
                                .fillMaxSize()
                                .align(Alignment.Center)
                                .clickable { isShowProfilePreview = true },
                        )
                    }
                }
                // 프로필 명
                Text(
                    text = stringResource(R.string.txt_user_name_tail, userNickname),
                    style = MaterialTheme.typography.caption
                        .copy(
                            fontSize = 16.sp,
                            color = mono800
                        )
                )
                // 편집 버튼
                Button(
                    onClick = { showMenuProfile = true },
                    modifier = Modifier
                        .width(56.dp)
                        .height(24.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = purple200,
                        contentColor = contentColorFor(backgroundColor),
                        disabledBackgroundColor = MaterialTheme
                            .colors.onSurface
                            .copy(alpha = 0.12f)
                            .compositeOver(purple300),
                        disabledContentColor = mono300.copy(alpha = ContentAlpha.disabled)
                    ),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(0.dp),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_create_todo),
                            contentDescription = "",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = stringResource(id = R.string.txt_edit),
                            fontSize = 10.sp,
                            style = MaterialTheme.typography.caption.copy(
                                color = white
                            )
                        )
                    }
                }
            }
            // 일일 달성 목표
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(vertical = 12.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.txt_today_task),
                    style = MaterialTheme.typography.subtitle1.copy(
                        color = black,
                        textAlign = TextAlign.Start
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(horizontal = 4.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Text(
                        text = stringResource(R.string.txt_today_task_state),
                        style = MaterialTheme.typography.caption
                            .copy(
                                color = black,
                            )
                    )
                    // 목표 달성량
                    Text(
                        text = "$todoState / $todoGoal",
                        style = MaterialTheme.typography.caption
                            .copy(
                                color = black,
                            )
                    )
                }
            }
            ToitPointCard(toitPoint)
            Spacer(modifier = Modifier.height(32.dp))
            UserPhrasesUnit(dayCnt, userNickname)
            Spacer(modifier = Modifier.height(32.dp))

        }

        // admobs
        AdmobBanner(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
        )
    }
}

@Composable
fun UserPhrasesUnit(
    dayCnt:Int,
    userNickname:String
){
    // 문구
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            modifier = Modifier
                .wrapContentSize()
                .padding(bottom = 12.dp),
            verticalAlignment = Alignment.Bottom
        ){
            // ToIt 과 함께한지 123 일 째!
            Text(
                text = stringResource(R.string.txt_day_count1),
                style = MaterialTheme.typography.caption
                    .copy(
                        mono900,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    ),
                modifier = Modifier
                    .wrapContentSize()
            )
            Text(
                text = parseNumberString(dayCnt),
                style = MaterialTheme.typography.caption
                    .copy(
                        mono900,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Medium
                    ),
                modifier = Modifier
                    .wrapContentSize()
                    .padding(horizontal = 4.dp)
            )
            Text(
                text = stringResource(R.string.txt_day_count2),
                style = MaterialTheme.typography.caption
                    .copy(
                        mono900,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    ),
                modifier = Modifier.wrapContentSize()
            )
        }
        Text(
            text = stringResource(R.string.txt_day_count3),
            style = MaterialTheme.typography.caption
                .copy(
                    mono900,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                ),
            modifier = Modifier
                .wrapContentSize()
                .padding(vertical = 4.dp)
        )
        Text(
            text = stringResource(R.string.txt_day_count4, userNickname),
            style = MaterialTheme.typography.caption
                .copy(
                    mono900,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                ),
            modifier = Modifier
                .wrapContentSize()
                .padding(vertical = 4.dp)
        )
    }
}

@Composable
fun ToitPointCard(toitPoint:Int){
    // 뱃지 배경
    val colorList = listOf(red100, navy300, purple200, orange200)
    val brush = Brush.linearGradient(colorList)
    Card(
        elevation = 1.dp,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .width(256.dp)
            .wrapContentHeight(),
//        backgroundColor = none
    ){
        Box(modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(brush = brush)
        ){
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .align(Alignment.TopStart)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(top = 16.dp, bottom = 4.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Toit Point \uD83D\uDD25",
                        style = MaterialTheme.typography.caption
                            .copy(
                                white,
                                fontSize = 16.sp,
                                textAlign = TextAlign.Center
                            ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                    )
                }
                Row(
                    Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(top = 4.dp, bottom = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = parseNumberString(toitPoint),
                        style = MaterialTheme.typography.caption
                            .copy(
                                white,
                                fontSize = 20.sp,
                                textAlign = TextAlign.Center
                            ),
                        modifier = Modifier
                            .wrapContentSize()
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "point",
                        style = MaterialTheme.typography.caption
                            .copy(
                                white,
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center
                            ),
                        modifier = Modifier
                            .wrapContentSize()
                    )
                }
            }
        }
    }
}

fun parseNumberString(point:Int):String{
    val numberFormat = NumberFormat.getInstance()
    return numberFormat.format(point)
}
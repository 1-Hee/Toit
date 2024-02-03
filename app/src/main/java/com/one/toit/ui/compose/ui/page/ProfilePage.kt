package com.one.toit.ui.compose.ui.page

import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.material.Text
import androidx.compose.material.contentColorFor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.one.toit.R
import com.one.toit.ui.compose.style.black
import com.one.toit.ui.compose.style.mono100
import com.one.toit.ui.compose.style.mono300
import com.one.toit.ui.compose.style.mono800
import com.one.toit.ui.compose.style.mono900
import com.one.toit.ui.compose.style.purple200
import com.one.toit.ui.compose.style.purple300
import com.one.toit.ui.compose.style.white
import com.one.toit.ui.compose.ui.unit.profile.EditNickNameDialog
import com.one.toit.ui.compose.ui.unit.profile.ProfileMenuDialog
import com.one.toit.util.AppUtil
import com.one.toit.util.PreferenceUtil
import timber.log.Timber

@Preview(showBackground = true)
@Composable
fun ProfilePage() {
    Timber.plant(Timber.DebugTree())
    val context = LocalContext.current
    val prefs = PreferenceUtil.getInstance(context)
    val profileImgKey = stringResource(id = R.string.key_profile_img)
    val pickSuccess = stringResource(id = R.string.ts_picked_img)
    val pickError = stringResource(id = R.string.ts_error_pick_img)
    // 사진 불러오기 기능
    // 이미지 불러오기
//    val photoIntent =
//        // ACTION_GET_CONTENT
//        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
//            type = "image/*"
//            action = Intent.ACTION_PICK
//            putExtra(
//                Intent.EXTRA_MIME_TYPES,
//                arrayOf("image/jpeg", "image/png", "image/bmp", "image/webp")
//            )
//            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
//        }
//
//    val photoLauncher = // 갤러리에서 사진 가져오기
//        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//            if (result.resultCode == Activity.RESULT_OK) {
//                result.data?.data?.let { uri ->
//                    Timber.i("picked uri %s", uri)
//                    Timber.i("uri path %s", uri.path)
//                    Timber.i("encoded path %s", uri.encodedPath)
//                    val file = File(uri.path)
//                    Timber.i("real path? : %s", file.absolutePath)
//                    AppUtil.toast(context, pickSuccess)
//                    prefs.setValue(profileImgKey, uri.toString())
//                } ?: run { AppUtil.toast(context, pickError) }
//            } else if (result.resultCode != Activity.RESULT_CANCELED) {
//                // ??
//            }
//        }

    /**
     * 프로필 정보 관련
     */
    val nickNameKey = stringResource(id = R.string.key_nickname)
    var userNickname by remember { mutableStateOf(prefs.getValue(nickNameKey)) }
    var userProfile by remember { mutableStateOf(prefs.getValue(profileImgKey)) }

    var todoState by remember { mutableStateOf(5) }
    var todoGoal by remember { mutableStateOf(10) }
    var toitPoint by remember { mutableStateOf(2024) }
    var totalTodo by remember { mutableStateOf(127) }
    var avgTodo by remember { mutableStateOf(8.7) }
    var minRecord by remember { mutableStateOf(780) } // second
    var maxRecord by remember { mutableStateOf(26580) } // second
    var avgRecord by remember { mutableStateOf(11460) } // second
    var axiomString by remember { mutableStateOf("성공은 작은 노력의 쌓임입니다.\n오늘 하루도 조금 더 나아가세요.") }
    // 다이얼로그 창
    var showMenuProfile by remember { mutableStateOf(false) }
    var showEditNickName by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            Timber.i("image : %s", uri.toString())
            // 실제 파일 경로를 얻기 위해 ContentResolver 사용
            val realPath = getRealPathFromURI(context.contentResolver, it)
            realPath.let { filePath ->
                Timber.i("realPath : %s ", filePath)
                prefs.setValue(profileImgKey, filePath)
                Timber.i("userProfile : %s", userProfile)
            }
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
                // photoLauncher.launch(photoIntent)
                launcher.launch("image/*")
            },
            onInitProfile = {}
        )
    }
    if(showEditNickName){
        EditNickNameDialog {
            showEditNickName = false
            userNickname = prefs.getValue(nickNameKey)
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
                .padding(horizontal = 16.dp, vertical = 12.dp)
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
                        /**
                         * 현재 intent를 통해서 uri를 콜백함수로 처리하면
                         * 콘텐츠 프로바이더를 통해 가져오는 것과 다른 주소를 리턴하고
                         * 결과적으로 파싱 함수를 통해 변환하는 것이 불가능해짐
                         */

                        val defPainter = painterResource(id = R.drawable.ic_profile)
                        val painter = if(userProfile.isBlank()){
                            Timber.i("userProfile is empty!!")
                            defPainter
                        }else {
                            try {
                                Timber.i("user profile : %s", userProfile)
                                val uri = Uri.parse(userProfile)
                                val bitmap = AppUtil.Image.getBitmap(uri, context.contentResolver)
                                if(bitmap == null) defPainter
                                else {
                                    BitmapPainter(bitmap.asImageBitmap())
                                }
                            }catch (e : Exception){
                                Timber.e("[msg] : %s", e.message)
                                Timber.e("parsing error!!!!")
                                defPainter
                            }
                        }

//                        val list = AppUtil.Image.loadImage(context.contentResolver)
//                        Timber.i("[list] : %s", list)
//                        prefs.setValue(profileImgKey, list[0].savedAddress);
//                        try {
//                            Timber.i("adddress : %s", userProfile)
//                            val uri = Uri.parse(userProfile);
//                            Timber.i("profile uri : %s", uri)
//                            val bitmap = AppUtil.Image.getBitmap(uri, context.contentResolver)
//                            Timber.i("user bitmap : %s", bitmap)
//                            if(bitmap != null){
//                                painter = BitmapPainter(bitmap.asImageBitmap())
//                            }
//
//                        }catch (e:Exception){
//
//                        }
//                        val bitmap =  if(userProfile.isBlank()){
//                            Timber.e("no data for user profile!!!!!!!!!!!!")
//                            null
//                        }else {
//                            try {
//                                val uri = Uri.parse(userProfile)
//                                val parseBitmap = AppUtil.Image.getBitmap(uri, context.contentResolver)
//                                Timber.i("success parse Bitmap.. %s %s %s",
//                                    parseBitmap?.width, parseBitmap?.height, parseBitmap?.density)
//                                parseBitmap?.asImageBitmap()
//                            }catch (e:Exception){
//                                Timber.e("profile path : %s", userProfile)
//                                Timber.e("parsing error!!!!!!!!!!!....")
//                                Timber.e("cause :%s", e.cause)
//                                Timber.e("msg : %s", e.message)
//                                null
//                            }
//                        }
//                        val painter = if(bitmap == null) {
//                            painterResource(id = R.drawable.ic_profile)
//                        }else {
//                            BitmapPainter(bitmap)
//                        }
                        Image(
                            painter = painter,
                            contentDescription = "profileImage",
                            modifier = Modifier
                                .fillMaxSize()
                                .align(Alignment.Center),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
                // 프로필 명
                Text(
                    text = "$userNickname 님",
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
                            text = "편집",
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
                    text = "일일 분석",
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
                        text = "목표 달성",
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
            // 레코드 보드
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(vertical = 12.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "레코드 보드",
                    style = MaterialTheme.typography.subtitle1
                        .copy(
                            color = black,
                            textAlign = TextAlign.Start
                        ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                )
                // toit, 전체 목표, 평균 목표
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    RecordBox("Toit!", "$toitPoint", true)
                    Box(modifier = Modifier
                        .width(1.dp)
                        .height(24.dp)
                        .background(mono300))
                    RecordBox("전체 목표", "$totalTodo" )
                    Box(modifier = Modifier
                        .width(1.dp)
                        .height(24.dp)
                        .background(mono300))
                    RecordBox("평균 목표", "$avgTodo" )
                }
                // 최단기록, 최장기록, 평균 기록
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    RecordBox("최단 기록", calcTimeString(minRecord.toLong()))
                    Box(modifier = Modifier
                        .width(1.dp)
                        .height(24.dp)
                        .background(mono300))
                    RecordBox("최장 기록", calcTimeString(maxRecord.toLong()))
                    Box(modifier = Modifier
                        .width(1.dp)
                        .height(24.dp)
                        .background(mono300))
                    RecordBox("평균 기록", calcTimeString(avgRecord.toLong()))
                }
            }

            // 격언
            Text(
                text = axiomString,
                style = MaterialTheme.typography.caption
                    .copy(
                        mono900,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    ),
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(vertical = 48.dp)
            )
        }
    }
}

@Composable
fun RecordBox(title:String, content:String, hasHelp:Boolean = false){
    Box(modifier = Modifier
        .wrapContentWidth()
        .height(48.dp)){
        Row(
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.TopCenter)
            ,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.caption
                    .copy(
                        color = black,
                        fontSize = 16.sp
                    )
            )
            if(hasHelp){
                Icon(
                    Icons.Rounded.Info,
                    contentDescription = "icon",
                    tint = mono900,
                    modifier = Modifier.size(16.dp)
                )

            }
        }
        Text(
            text = content,
            style = MaterialTheme.typography.caption
                .copy(
                    color = mono900
                ),
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.BottomCenter)
        )
    }
}

fun calcTimeString(time:Long):String{
    val hour  = ( time / 60 ) / 60;
    val min = (time / 60) % 60;
    return "$hour:$min"
}


// ContentResolver를 사용하여 URI에서 실제 파일 경로 얻기
private fun getRealPathFromURI(contentResolver: ContentResolver, uri: Uri): String {
    var realPath = ""
    val projection = arrayOf(MediaStore.Images.Media._ID)
    val cursor: Cursor = contentResolver.query(uri, projection, null, null, null) ?: return ""
    val idColum:Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
    cursor.use {
        if (it.moveToFirst()) {
            val id:Long = cursor.getLong(idColum)
            val contentUri:Uri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id.toString())
            realPath = contentUri.toString();
        }
    }
    return realPath
}

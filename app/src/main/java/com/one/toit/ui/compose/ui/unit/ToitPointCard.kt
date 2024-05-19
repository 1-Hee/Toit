package com.one.toit.ui.compose.ui.unit

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.one.toit.R
import com.one.toit.ui.compose.style.black
import com.one.toit.ui.compose.style.blue100
import com.one.toit.ui.compose.style.green100
import com.one.toit.ui.compose.style.mono600
import com.one.toit.ui.compose.style.orange100
import com.one.toit.ui.compose.style.purple100
import com.one.toit.ui.compose.style.red100
import com.one.toit.util.PreferenceUtil
import java.text.NumberFormat
import kotlin.random.Random

@Composable
fun ToitPointCard(
    modifier: Modifier = Modifier,
    toitPoint:Long
){

    /**
     * 프로필 정보 관련
     */
    val context = LocalContext.current
    val prefs = PreferenceUtil.getInstance(context)
    val nickNameKey = stringResource(id = R.string.key_nickname)
    var userNickname by remember { mutableStateOf(prefs.getValue(nickNameKey)) } // 사용자 닉네임

    val numberFormat = NumberFormat.getInstance()
    val pointText = numberFormat.format(toitPoint) + " P"
    var imgIndex by remember { mutableStateOf(0) }
    var bgIndex by remember { mutableStateOf(0) }
    val guideText = stringResource(id = R.string.txt_guide_toit_point, userNickname)

    LaunchedEffect(Unit) {
        imgIndex = Random.nextInt(0, 3)
        bgIndex = Random.nextInt(0, 5)
    }
    val imgAsset = when(imgIndex){
        1 -> painterResource(id = R.drawable.img_bag)
        2 -> painterResource(id = R.drawable.img_pig)
        else -> painterResource(id = R.drawable.img_water)
    }
    val bgColor = when(bgIndex){
        1 -> blue100
        2 -> orange100
        3 -> green100
        4 -> red100
        else -> purple100
    }

    Card(
        modifier = Modifier
            .width(228.dp)
            .height(256.dp),
        backgroundColor = bgColor,
        elevation = 2.dp,
        contentColor = black,
        shape = RoundedCornerShape(8.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = imgAsset,
                contentDescription = "icon",
                modifier = Modifier
                    .size(96.dp),
                contentScale = ContentScale.Inside
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = pointText,
                style = MaterialTheme.typography.caption
                    .copy(
                        fontSize = 24.sp,
                        color = black,
                        textAlign = TextAlign.Center
                    ),
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(horizontal = 8.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = guideText,
                style = MaterialTheme.typography.caption
                    .copy(
                        fontSize = 10.sp,
                        color = mono600,
                        textAlign = TextAlign.Center
                    ),
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(horizontal = 8.dp)
            )
        }

    }
}
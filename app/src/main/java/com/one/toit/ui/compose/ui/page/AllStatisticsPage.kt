@file:OptIn(ExperimentalLayoutApi::class)

package com.one.toit.ui.compose.ui.page

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.contentColorFor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.one.toit.R
import com.one.toit.data.dto.TaskDTO
import com.one.toit.data.viewmodel.TaskViewModel
import com.one.toit.ui.compose.style.black
import com.one.toit.ui.compose.style.mono100
import com.one.toit.ui.compose.style.mono300
import com.one.toit.ui.compose.style.mono400
import com.one.toit.ui.compose.style.mono50
import com.one.toit.ui.compose.style.mono600
import com.one.toit.ui.compose.style.mono800
import com.one.toit.ui.compose.style.mono900
import com.one.toit.ui.compose.style.none
import com.one.toit.ui.compose.style.purple200
import com.one.toit.ui.compose.style.purple50
import com.one.toit.ui.compose.style.white
import com.one.toit.ui.compose.ui.unit.ToitPointCard
import com.one.toit.ui.compose.ui.unit.todo.ItemNoContent
import com.one.toit.ui.compose.ui.unit.todo.ItemTodo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import kotlin.math.roundToInt

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AllStatisticsPage(
    navController: NavHostController,
    taskViewModel: TaskViewModel,
    launcher: ActivityResultLauncher<Intent>? = null
){
    val context = LocalContext.current
    // scroll state
    val outerScrollState = rememberScrollState()
    // tab index
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    // 현재 높이를 저장하기 위한 상태 변수
    var deviceWidth by remember { mutableIntStateOf(0) }
    var deviceHeight by remember { mutableIntStateOf(0) }
    // LocalDensity를 사용하여 현재 디바이스의 화면 밀도를 가져옴
    val density = LocalDensity.current.density
    // 현재 높이를 구하는 코드
    LaunchedEffect(Unit) {
        val displayMetrics = context.resources.displayMetrics
        deviceHeight = (displayMetrics.heightPixels / density).toInt()
        deviceWidth = (displayMetrics.widthPixels / density).toInt()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(white)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        // TabLayoutExample()

        TabRow(
            selectedTabIndex = selectedTabIndex,
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = white,
            contentColor = contentColorFor(backgroundColor),
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    modifier = Modifier
                        .tabIndicatorOffset(tabPositions[selectedTabIndex])
                        .padding(horizontal = 8.dp),
                    color = purple200
                )
            },
            tabs = {
                Tab(
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(8.dp),
                    selected = selectedTabIndex == 0,
                    onClick = { selectedTabIndex = 0 },
                    text = {
                        Text(
                            text = "ToIt! Dashboard",
                            style = MaterialTheme.typography.caption.copy(
                                color = black,
                                fontSize = 16.sp
                            ),
                        )
                    },
                    selectedContentColor =  mono100,
                    unselectedContentColor = white
                )
                Tab(
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(8.dp),
                    selected = selectedTabIndex == 1,
                    onClick = { selectedTabIndex = 1 },
                    text = {
                        Text(
                            text = "ToIt LIST",
                            style = MaterialTheme.typography.caption.copy(
                                color = black,
                                fontSize = 16.sp
                            ),
                        )
                    },
                    selectedContentColor =  mono100,
                    unselectedContentColor = white
                )
            }

        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(outerScrollState),
        ) {
            // Content of each tab
            when (selectedTabIndex) {
                0 -> {
                    // TODO db 와 함께 실제 값으로 변경
                    val mList = listOf(
                        "1234 개", // 전체 목표 수
                        "1234 개", // 평균 일일 목표 수
                        "1234 개", // 일일 목표 중앙 값
                        "87.52%", // 전체 목표 달성 율
                        "84.52%", // 평균 일일 목표 달성율
                        "86.52%", // 일일 목표 달성율 중앙 값
                        "12:34", // 최장 목표 달성 시간
                        "08:24", // 평균 목표 달성 시간
                        "09:14", // 평균 목표 달성 시간 중앙 값
                        )
                    val mSummaryMap = getSummaryMap(context, mList)
                    TotalSummaryUnit(mSummaryMap)
                }
                1 -> {
                    TodoSearchUnit(context, taskViewModel, launcher)
                }
            }
        }

    }
}

@Composable
fun TodoSearchUnit(
    context: Context,
    taskViewModel:TaskViewModel,
    launcher: ActivityResultLauncher<Intent>? = null
){
    val scrollState = rememberScrollState()
    // MutableState를 사용하여 taskDTOList를 감싸기
    val taskDTOListState = remember { mutableStateOf<List<TaskDTO>>(emptyList()) }
    LaunchedEffect(Unit) {
        withContext(Dispatchers.Main) {
            val taskList = taskViewModel.readTaskList()
            taskDTOListState.value = taskList.map { task ->
                TaskDTO(
                    task.register.taskId,
                    task.register.createAt.toString(),
                    task.info.infoId,
                    task.info.taskTitle,
                    task.info.taskMemo,
                    task.info.taskLimit,
                    task.info.taskComplete,
                    task.info.taskCertification
                )
            }
        }
    }
    // taskDTOListState를 사용하여 UI 업데이트
    val taskDTOList = taskDTOListState.value
    SearchUnit()
    sortOption(context)

    // 현재 높이를 저장하기 위한 상태 변수
    var deviceHeight by remember { mutableIntStateOf(0) }
    // LocalDensity를 사용하여 현재 디바이스의 화면 밀도를 가져옴
    val density = LocalDensity.current.density
    // 현재 높이를 구하는 코드
    LaunchedEffect(Unit) {
        val displayMetrics = context.resources.displayMetrics
        deviceHeight = (displayMetrics.heightPixels / density).toInt()
    }
    // 등록한 List가 있을 경우
    if(taskDTOList.isNotEmpty()){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height((deviceHeight * 0.7).dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Spacer(modifier = Modifier.height(4.dp))
            repeat(taskDTOList.size){
                ItemTodo(taskDTO = taskDTOList[it], launcher = launcher)
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }else {
        ItemNoContent()
    }
}

@Composable
fun sortOption(
    context: Context
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
            options.forEach { option ->
                DropdownMenuItem(
                    onClick = {
                        selectedOption = option
                        expanded = false
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

// 검색 창
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchUnit(){
    var searchKeyWord by remember { mutableStateOf("") }
    val searchHint = stringResource(R.string.txt_hint_search_todo)
    val searchBoxColors =  TextFieldDefaults.textFieldColors(
        backgroundColor = none,
        cursorColor = none,
        errorCursorColor = none,
        focusedIndicatorColor = purple200,
        unfocusedIndicatorColor = none
    )

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    Spacer(modifier = Modifier.height(24.dp))
    Box(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .background(color = mono50, RoundedCornerShape(8.dp))
        .padding(horizontal = 12.dp, vertical = 8.dp)
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
            TextField(
                modifier = Modifier
                    .height(48.dp)
                    .focusRequester(focusRequester)
                ,
                value = searchKeyWord,
                onValueChange = { searchKeyWord = it },
                placeholder = {
                    Text(
                        text = searchHint,
                        style = MaterialTheme.typography.caption.copy(
                            color = mono400,
                            fontSize = 12.sp
                        ),
                    )
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                maxLines = 28,
                singleLine = true,
                colors = searchBoxColors
            )
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


// 통계 요약을 위한 파싱 함수
fun getSummaryMap(context: Context, valueList:List<String>):Map<String, String>{
    val tagList = context.resources.getStringArray(R.array.statistics_chart_item_list)
    val mSummaryMap = mutableMapOf<String, String>()
    tagList.forEachIndexed { index, tag ->
        mSummaryMap[tag] = valueList[index]
    }
    return mSummaryMap
}


// 전체 통계 대쉬보드
@Composable
fun TotalSummaryUnit(
    statisticDataMap:Map<String, String>
){
    // TODO dummy 제거...
    Text(
        text = stringResource(R.string.title_toit_heatmap),
        style = MaterialTheme.typography.caption.copy(
            color = black,
            fontSize = 16.sp
        ),
        modifier = Modifier.padding(vertical = 16.dp)
    )
    FlowRow(
        modifier = Modifier
            .wrapContentWidth()
            .wrapContentHeight()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.Top,
        horizontalArrangement = Arrangement.End
    ){
        repeat(65){
            HeatMapUnit()
        }
    }
    Text(
        text = stringResource(R.string.txt_guide_heatmap),
        style = MaterialTheme.typography.caption.copy(
            color = mono600,
            fontSize = 12.sp,
            textAlign = TextAlign.End
        ),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    )
    // Toit point...
    val toitPoint = 123456 // todo 계산 하여 실제 값으로...
    Spacer(modifier = Modifier.height(32.dp))
    Box(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
    ){
        ToitPointCard(
            modifier = Modifier.align(Alignment.Center),
            toitPoint = toitPoint
        )
    }
    Spacer(modifier = Modifier.height(48.dp))
    // 통계 차트
    /**
     * 통계 데이터
     */
    StatisticalSummary(statisticDataMap)
    Spacer(modifier = Modifier.height(32.dp))
}
@Composable
// 통계 차트
fun StatisticalSummary(
    statisticDataMap:Map<String, String>
) {
    Text(
        text = stringResource(R.string.title_statistics_chart),
        style = MaterialTheme.typography.caption.copy(
            color = black,
            fontSize = 16.sp,
            textAlign = TextAlign.Start
        ),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(vertical = 8.dp),
    )
    Divider(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        color = mono300,
        thickness = 1.dp
    )
    /**
     * 통계 데이터들...
     */
    statisticDataMap.forEach { (key, value) ->
        StatisticalDataUnit(
            key,
            value,
            width = 128.dp
        )
    }
}

@Composable
fun HeatMapUnit(
    modifier: Modifier = Modifier,
    size: Dp = 24.dp,
    color: Color = purple50,
    borderColor:Color = mono900,
    borderSize:Dp = 1.dp
){
    Box(modifier = modifier
        .size(size)
        .background(color)
        .border(borderSize, borderColor)
    )
}
@Composable
fun StatisticalDataUnit(
    label:String = "",
    value:String = "",
    dotColor:Color = mono300,
    thickness:Dp = 1.dp,
    step: Dp = 2.dp,
    width:Dp = 96.dp
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.caption.copy(
                color = black,
                fontSize = 16.sp,
            ),
        )
        Box(modifier = Modifier
            .width(width)
            .height(thickness)
            .background(dotColor, shape = DottedShape(step)))
        Text(
            text = value,
            style = MaterialTheme.typography.caption.copy(
                color = mono800,
                fontSize = 16.sp,
            ),
        )

    }
}

private data class DottedShape(
    val step: Dp,
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ) = Outline.Generic(Path().apply {
        val stepPx = with(density) { step.toPx() }
        val stepsCount = (size.width / stepPx).roundToInt()
        val actualStep = size.width / stepsCount
        val dotSize = Size(width = actualStep / 2, height = size.height)
        for (i in 0 until stepsCount) {
            addRect(
                Rect(
                    offset = Offset(x = i * actualStep, y = 0f),
                    size = dotSize
                )
            )
        }
        close()
    })
}


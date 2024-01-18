package com.one.toit.compose.nav

import com.one.toit.R

sealed class BoardRoute(
    val title: Int, val route:String
) {
    object BoardWrite: BoardRoute(title = R.string.p_board_write, route = "BoardWritePage")
}
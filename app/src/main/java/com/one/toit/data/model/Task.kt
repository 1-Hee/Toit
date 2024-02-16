package com.one.toit.data.model

import androidx.room.Embedded

data class Task(
    @Embedded val register:TaskRegister,
    @Embedded val info:TaskInfo
)

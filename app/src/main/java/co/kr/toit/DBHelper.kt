package co.kr.toit

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper : SQLiteOpenHelper {
    constructor(context: Context) : super(context, "main_task.db", null, 1)

    override fun onCreate(p0: SQLiteDatabase?) {

        val sql = """
            create table MainTaskTable
            (main_idx integer primary key autoincrement,
            main_task text not null,
            main_edit_date date not null,
            main_start_date date not null,
            main_start_time time not null,
            main_end_date date not null,
            main_end_time time not null,
            sub_task_number integer)
        """.trimIndent()

        p0?.execSQL(sql)

    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        
    }
}
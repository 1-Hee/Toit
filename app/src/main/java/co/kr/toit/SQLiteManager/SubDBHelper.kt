package co.kr.toit.SQLiteManager

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SubDBHelper : SQLiteOpenHelper {
    constructor(context: Context) : super(context, "sub_task.db", null, 1)

    override fun onCreate(p0: SQLiteDatabase?) {

        val sql = """
            create table SubTaskTable
            (sub_idx integer primary key autoincrement,
            sub_task text not null,
            FOREIGN KEY (main_idx) REFERENCES MainTaskTable (main_idx))
        """.trimIndent()

        p0?.execSQL(sql)

    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {

    }
}
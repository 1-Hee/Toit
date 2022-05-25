package co.kr.toit

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper : SQLiteOpenHelper {
    constructor(context: Context) : super(context, "memo.db", null, 1)

    override fun onCreate(p0: SQLiteDatabase?) {

        val sql = """
            create table RecordTable
            (rec_idx integer primary key autoincrement,
            rec_subject text not null,
            rec_curr date not null,
            rec_date1 date not null,
            rec_date2 date not null,
            rec_time1 time not null,
            rec_time2 time not null,
            rec_impo_stars float not null,
            rec_urg_stars float not null,            
            rec_memo text)
        """.trimIndent()

        p0?.execSQL(sql)

    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        
    }
}
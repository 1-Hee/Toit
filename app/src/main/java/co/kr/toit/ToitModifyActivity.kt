package co.kr.toit

import android.app.TimePickerDialog
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.WindowManager
import androidx.core.util.Pair
import co.kr.toit.databinding.ActivityToitModifyBinding
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

class ToitModifyActivity : AppCompatActivity() {

    lateinit var b : ActivityToitModifyBinding

    var myTimer : Calendar = Calendar.getInstance()
    val myTimePicker = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
        myTimer.set(Calendar.HOUR_OF_DAY, hour)
        myTimer.set(Calendar.MINUTE, minute)
        updateTime()
    }
    var TimerSwitch = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityToitModifyBinding.inflate(layoutInflater)
        setContentView(b.root)

        setSupportActionBar(b.modiToolbar)
        title = ""


        // 상태창 색깔 코드로 바꾸기
        if (Build.VERSION.SDK_INT >= 21) {
            val window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = this.resources.getColor(R.color.SecondaryColor) 
        }

        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        b.modiCloseBtn.setOnClickListener {
            finish()
        }

        val helper =DBHelper(this)
        val sql = """
            select rec_subject, rec_curr, rec_date1, rec_date2, rec_time1, rec_time2,
            rec_impo_stars, rec_urg_stars, rec_memo
            from RecordTable
            where rec_idx=?
        """.trimIndent()

        val rec_idx = intent.getIntExtra("rec_idx", 0)
        val args = arrayOf(rec_idx.toString())
        val c1 = helper.writableDatabase.rawQuery(sql, args)
        c1.moveToNext()

        val idx1 = c1.getColumnIndex("rec_subject")
        val idx2 = c1.getColumnIndex("rec_curr")
        val idx3 = c1.getColumnIndex("rec_date1")
        val idx4 = c1.getColumnIndex("rec_date2")
        val idx5 = c1.getColumnIndex("rec_time1")
        val idx6 = c1.getColumnIndex("rec_time2")
        val idx7 = c1.getColumnIndex("rec_impo_stars")
        val idx8 = c1.getColumnIndex("rec_urg_stars")
        val idx9 = c1.getColumnIndex("rec_memo")

        var rec_subject = c1.getString(idx1)
        val rec_curr = c1.getString(idx2)
        val rec_date1 = c1.getString(idx3)
        val rec_date2 = c1.getString(idx4)
        val rec_time1 = c1.getString(idx5)
        val rec_time2 = c1.getString(idx6)
        val rec_impo_stars = c1.getFloat(idx7)
        val rec_urg_stars = c1.getFloat(idx8)
        val rec_memo = c1.getString(idx9)

        helper.writableDatabase.close()

        b.modiInputText.setText(rec_subject)
        b.modiInputDate1.setText(rec_date1)
        b.modiInputDate2.setText(rec_date2)
        b.modiInputTime1.setText(rec_time1)
        b.modiInputTime2.setText(rec_time2)
        b.modiRatingBar1.rating = rec_impo_stars
        b.modiRatingBar2.rating = rec_urg_stars
        b.modiMemo.setText(rec_memo)
        b.modiEditdate.text = rec_curr

        b.modiInputDate1.setOnClickListener { showDateRangePicker() }
        b.modiInputDate2.setOnClickListener { showDateRangePicker() }

        b.modiInputTime1.setOnClickListener{
            TimerSwitch = true
            ShowDialog()
        }

        b.modiInputTime2.setOnClickListener{
            TimerSwitch = false
            ShowDialog()
        }

        b.modiSaveBtn.setOnClickListener {

            val helper = DBHelper(this)
            val sql = """
                    update RecordTable
                    set rec_subject = ?, rec_curr = ?, rec_date1 = ?, rec_date2 = ?, 
                    rec_time1 = ?, rec_time2 = ?, rec_impo_stars = ?, rec_urg_stars= ?, 
                    rec_memo = ?
                    where rec_idx = ?
                """.trimIndent()

            val sdf = SimpleDateFormat("yyyy년 MM월 dd일 a h:mm", Locale.getDefault())
            val rec_curr = sdf.format(Date())
            val arg1 = arrayOf(
                b.modiInputText.text.toString(), rec_curr,
                b.modiInputDate1.text.toString(), b.modiInputDate2.text.toString(),
                b.modiInputTime1.text.toString(), b.modiInputTime2.text.toString(),
                b.modiRatingBar1.rating, b.modiRatingBar2.rating,
                b.modiMemo.text.toString(), rec_idx.toString()
            )

            helper.writableDatabase.execSQL(sql, arg1)
            helper.writableDatabase.close()

            finish()
        }

    }

    fun showDateRangePicker() {

        val dateRangePicker = MaterialDatePicker.Builder
            .dateRangePicker()
            .setTheme(R.style.DateDialog)
            .build()

        dateRangePicker.show(supportFragmentManager, "support")

        dateRangePicker.addOnPositiveButtonClickListener { datePicked ->
            val start_date = datePicked.first
            val end_date = datePicked.second
            val df = "yyyy년 MM월 dd일"
            val sdf = SimpleDateFormat(df, Locale.KOREA)
            b.modiInputDate1.setText(sdf.format(start_date))
            b.modiInputDate2.setText(sdf.format(end_date))
        }

    }

    fun updateTime() {
        val myFormat = "a h:mm" // 출력형식  00:00 PM
        val sdf = SimpleDateFormat(myFormat, Locale.KOREA)
        if(TimerSwitch){
            b.modiInputTime1.setText(sdf.format(myTimer.time))
        } else {
            b.modiInputTime2.setText(sdf.format(myTimer.time))
        }
    }

    fun ShowDialog() {
        TimePickerDialog(
            this@ToitModifyActivity,
            myTimePicker,
            myTimer[Calendar.HOUR],
            myTimer[Calendar.MINUTE],
            false
        ).show()
    }


    // 툴바의 홈버튼 누르면 홈으로 돌아가게 하는 메서드
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }

        return super.onOptionsItemSelected(item)
    }
}
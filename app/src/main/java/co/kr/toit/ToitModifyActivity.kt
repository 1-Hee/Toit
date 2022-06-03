package co.kr.toit

import android.app.TimePickerDialog
import android.content.Intent
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
        title = "메모 편집 창"


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
            reload()
            finish()
        }

        val helper =DBHelper(this)
        val sql = """
            select main_task, main_edit_date, main_start_date,
            main_start_time, main_end_date, main_end_time             
            from MainTaskTable
            where main_idx=?
        """.trimIndent()

        val main_idx = intent.getIntExtra("main_idx", 0)
        val args = arrayOf(main_idx.toString())
        val c1 = helper.writableDatabase.rawQuery(sql, args)
        c1.moveToNext()

        val idx1 = c1.getColumnIndex("main_task")
        val idx2 = c1.getColumnIndex("main_edit_date")
        val idx3 = c1.getColumnIndex("main_start_date")
        val idx4 = c1.getColumnIndex("main_start_time")
        val idx5 = c1.getColumnIndex("main_end_date")
        val idx6 = c1.getColumnIndex("main_end_time")

        helper.writableDatabase.close()

        val MainTask = c1.getString(idx1)
        val EditDate = c1.getString(idx2)
        val startDate = c1.getString(idx3)
        val startTime = c1.getString(idx4)
        val endDate = c1.getString(idx5)
        val endTime = c1.getString(idx6)

        b.modiMainTaskInputText.setText(MainTask)
        b.modiEditdate.text = EditDate
        b.modiInputDate1.setText(startDate)
        b.modiInputTime1.setText(startTime)
        b.modiInputDate2.setText(endDate)
        b.modiInputTime2.setText(endTime)

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
                    update MainTaskTable
                    set main_task = ?, main_edit_date = ?, 
                    main_start_date = ?, main_start_time = ?,
                    main_end_date = ?, main_end_time = ?
                    where main_idx = ?
                """.trimIndent()

            val sdf = SimpleDateFormat("yyyy년 MM월 dd일 a h:mm", Locale.getDefault())

            val arg1 = arrayOf(
                b.modiMainTaskInputText.text.toString(), sdf.format(Date()).toString(),
                b.modiInputDate1.text, b.modiInputTime1.text,
                b.modiInputDate2.text, b.modiInputTime2.text, main_idx.toString()
            )

            helper.writableDatabase.execSQL(sql, arg1)
            helper.writableDatabase.close()

            reload()
            finish()
        }

    }

    private fun reload() {
        val mainActivity = Intent(this, MainActivity::class.java)
        startActivity(mainActivity)
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
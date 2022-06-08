package co.kr.toit.activity

import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import co.kr.toit.SQLiteManager.DBHelper
import co.kr.toit.R
import co.kr.toit.databinding.ActivityToitAddBinding
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

class ToitAddActivity : AppCompatActivity() {
    lateinit var b: ActivityToitAddBinding

    var myCalendar: Calendar = Calendar.getInstance()
    var myTimer: Calendar = Calendar.getInstance()
    val myTimePicker = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
        myTimer.set(Calendar.HOUR_OF_DAY, hour)
        myTimer.set(Calendar.MINUTE, minute)
        updateTime()
    }

    var TimerSwitch = true
    val currentTime = System.currentTimeMillis()
    var start_date = currentTime
    var end_date = currentTime
    var start_time: Date? = myTimer.time
    var end_time: Date? = myTimer.time


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityToitAddBinding.inflate(layoutInflater)
        setContentView(b.root)


        setSupportActionBar(b.addToolbar)
        title = "메인 프로젝트 추가"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        TimeDateInit()

        b.addCloseBtn.setOnClickListener {
            reload()
        }

        b.addInputDate1.setOnClickListener { showDateRangePicker() }
        b.addInputDate2.setOnClickListener { showDateRangePicker() }

        b.addInputTime1.setOnClickListener {
            TimerSwitch = true
            ShowDiallog()

        }

        b.addInputTime2.setOnClickListener {
            TimerSwitch = false
            ShowDiallog()
        }

        b.addSaveBtn.setOnClickListener {

            SQL_Add_SaveManager()
            reload()

        }
    }

    private fun SQL_Add_SaveManager(){

        val sdf1 = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val sdf2 = SimpleDateFormat("HH:mm", Locale.getDefault())
        val sdf3 = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())

        val startRawDate = Date(start_date)
        val startRawTime = start_time
        val endRawDate = Date(end_date)
        val endRawTime = end_time

        val StringStartDate = sdf1.format(startRawDate)
        val StringStartTime = sdf2.format(startRawTime)
        val StringEndDate = sdf1.format(endRawDate)
        val StringEndTime = sdf2.format(endRawTime)

        val StartDateArr = StringStartDate.split('-')
        val StartTimeArr = StringStartTime.split(':')
        val EndDateArr = StringEndDate.split('-')
        val EndTimeArr = StringEndTime.split(':')


        val year1 = Integer.parseInt(StartDateArr[0])
        val month1 = Integer.parseInt(StartDateArr[1])
        val day1 = Integer.parseInt(StartDateArr[2])
        val year2 = Integer.parseInt(EndDateArr[0])
        val month2 = Integer.parseInt(EndDateArr[1])
        val day2 = Integer.parseInt(EndDateArr[2])


        val hour1 = Integer.parseInt(StartTimeArr[0])
        val min1 = Integer.parseInt(StartTimeArr[1])
        val hour2 = Integer.parseInt(EndTimeArr[0])
        val min2 = Integer.parseInt(EndTimeArr[1])


        val CombinedStartDate1 = LocalDateTime.of(year1, month1, day1, hour1, min1)
        val CombinedStartDate2 = LocalDateTime.of(year2, month2, day2, hour2, min2)


        val StartDate = Date.from(CombinedStartDate1.atZone(ZoneId.systemDefault()).toInstant())
        val EndDate = Date.from(CombinedStartDate2.atZone(ZoneId.systemDefault()).toInstant())

        val main_start_date = sdf3.format(StartDate)
        val main_end_date = sdf3.format(EndDate)


        // 쿼리문
        val sql = """
                    insert into MainTaskTable
                    (main_task, main_edit_date, main_start_date, 
                    main_end_date) 
                    values(?, ?, ?, ?)
                """.trimIndent()

        // 데이터베이스 오픈
        val helper = DBHelper(this)

        // ? 에 설정될 값
        val arg1 = arrayOf(
            b.addMainTaskInputText.text.toString(), sdf3.format(Date()), main_start_date,
            main_end_date
        )

        // 저장
        helper.writableDatabase.execSQL(sql, arg1)
        helper.writableDatabase.close()

    }

    private fun reload() {
        val mainActivity = Intent(this, MainActivity::class.java)
        startActivity(mainActivity)
        finish()
    }

    fun ShowDiallog(){
        TimePickerDialog(
            this@ToitAddActivity,
            myTimePicker,
            myTimer[Calendar.HOUR],
            myTimer[Calendar.MINUTE],
            false
        ).show()
    }


    fun TimeDateInit() {
        val dateDefault = "yyyy년 MM월 dd일"
        val timeDefault = "a h:mm"

        val sdf1 = SimpleDateFormat(dateDefault, Locale.KOREA)
        val sdf2 = SimpleDateFormat(timeDefault, Locale.KOREA)

        b.addInputDate1.setText(sdf1.format(myCalendar.time))
        b.addInputDate2.setText(sdf1.format(myCalendar.time))
        b.addEditdate.text = sdf1.format(myCalendar.time) + " " + sdf2.format(myTimer.time)
        b.addInputTime1.setText(sdf2.format(myTimer.time))
        b.addInputTime2.setText(sdf2.format(myTimer.time))
    }

    fun updateTime() {
        val myFormat = "a h:mm" // 출력형식  00:00 PM
        val sdf = SimpleDateFormat(myFormat, Locale.KOREA)
        if(TimerSwitch){
            start_time = myTimer.time
            b.addInputTime1.setText(sdf.format(myTimer.time))
        } else {
            end_time = myTimer.time
            b.addInputTime2.setText(sdf.format(myTimer.time))
        }
    }

    fun showDateRangePicker() {

        val dateRangePicker = MaterialDatePicker.Builder
            .dateRangePicker()
            .setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
            .setTheme(R.style.DateDialog)
            .build()


        dateRangePicker.show(supportFragmentManager, "support")

        dateRangePicker.addOnPositiveButtonClickListener { datePicked ->
            start_date = datePicked.first
            end_date = datePicked.second
            val sdf = SimpleDateFormat("yyyy년 MM월 dd일", Locale.KOREA)
            b.addInputDate1.setText(sdf.format(start_date))
            b.addInputDate2.setText(sdf.format(end_date))
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        reload()
    }

    // 툴바의 홈버튼 누르면 홈으로 돌아가게 하는 메서드
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                reload()
                finish()
            }
        }

        return super.onOptionsItemSelected(item)
    }
}
package co.kr.toit

import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import co.kr.toit.databinding.ActivityToitAddBinding
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
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
            val sdf3 = SimpleDateFormat("yyyy년 MM월 dd일 a h:mm ", Locale.getDefault())

            // 쿼리문
            val sql = """
                    insert into MainTaskTable
                    (main_task, main_edit_date, main_start_date, 
                    main_start_time, main_end_date, main_end_time) 
                    values(?, ?, ?, ?, ? ,?)
                """.trimIndent()

            // 데이터베이스 오픈
            val helper = DBHelper(this)

            // ? 에 설정될 값
            val arg1 = arrayOf(
                b.addMainTaskInputText.text.toString(), sdf3.format(Date()),
                b.addInputDate1.text, b.addInputTime1.text, b.addInputDate2.text,
                b.addInputTime2.text)

            // 저장
            helper.writableDatabase.execSQL(sql, arg1)
            helper.writableDatabase.close()

            reload()
        }
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
        val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
        if(TimerSwitch){
            b.addInputTime1.setText(sdf.format(myTimer.time))
        } else {
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
            val start_date = datePicked.first
            val end_date = datePicked.second
            val df = "yyyy년 MM월 dd일"
            val sdf = SimpleDateFormat(df, Locale.KOREA)
            b.addInputDate1.setText(sdf.format(start_date))
            b.addInputDate2.setText(sdf.format(end_date))

        }

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
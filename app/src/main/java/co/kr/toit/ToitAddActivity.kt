package co.kr.toit

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import co.kr.toit.databinding.ActivityToitAddBinding
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.datepicker.MaterialDatePicker.Builder.dateRangePicker
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityToitAddBinding.inflate(layoutInflater)
        setContentView(b.root)

        title = "To do 생성하기"

        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        TimeDateInit()

        b.addCloseBtn.setOnClickListener {
            finish()
        }

        b.addSaveBtn.setOnClickListener {
            val rec_subject = b.addInputSubject.text
            val sdf3 = SimpleDateFormat("yyyy년 MM월 dd일", Locale.getDefault())
            val rec_curr = sdf3.format(Date())
            val rec_date1 = b.addInputDate1.text
            val rec_date2 = b.addInputDate2.text
            val rec_time1 = b.addInputTime1.text
            val rec_time2 = b.addInputTime2.text
            val rec_impo_stars = b.addRatingBar1.rating
            val rec_urg_stars = b.addRatingBar2.rating
            val rec_memo = b.addInputMemo.text


            // 쿼리문
            val sql = """
                    insert into Recordtable (rec_subject, rec_curr, rec_date1, rec_date2, rec_time1, rec_time2,
                    rec_impo_stars, rec_urg_stars, rec_memo)
                    values(?, ?, ?, ?, ?, ?, ?, ?)
                """.trimIndent()

            // 데이터베이스 오픈
            val helper = DBHelper(this)

            // ? 에 설정될 값
            val arg1 = arrayOf(
                rec_subject, rec_curr, rec_date1, rec_date2, rec_time1, rec_time2,
                rec_impo_stars, rec_urg_stars, rec_memo
            )

            // 저장
            helper.writableDatabase.execSQL(sql, arg1)
            helper.writableDatabase.close()

            finish()
        }

        b.addInputDate1.setOnClickListener { showDateRangePicker() }
        b.addInputDate2.setOnClickListener { showDateRangePicker() }

        b.addInputTime1.setOnClickListener {
            TimePickerDialog(
                this@ToitAddActivity,
                myTimePicker,
                myTimer[Calendar.HOUR],
                myTimer[Calendar.MINUTE],
                false
            ).show()
        }

        b.addInputTime2.setOnClickListener {
            TimePickerDialog(
                this@ToitAddActivity,
                myTimePicker,
                myTimer[Calendar.HOUR],
                myTimer[Calendar.MINUTE],
                false
            ).show()
        }

    }


    fun TimeDateInit() {
        val dateDefault = "yyyy년 MM월 dd일"
        val timeDefault = "a h:mm"

        val sdf1 = SimpleDateFormat(dateDefault, Locale.KOREA)
        val sdf2 = SimpleDateFormat(timeDefault, Locale.KOREA)

        b.addInputDate1.setText(sdf1.format(myCalendar.time))
        b.addInputDate2.setText(sdf1.format(myCalendar.time))
        b.addEditdate.text = sdf1.format(myCalendar.time)
        b.addInputTime1.setText(sdf2.format(myTimer.time))
        b.addInputTime2.setText(sdf2.format(myTimer.time))
    }

    fun updateTime() {
        val myFormat = "a h:mm a" // 출력형식  00:00 PM
        val sdf = SimpleDateFormat(myFormat, Locale.KOREA)
        b.addInputTime1.setText(sdf.format(myTimer.time))
        b.addInputTime2.setText(sdf.format(myTimer.time))
    }

    fun showDateRangePicker() {

        val constraintsBuilder =
            CalendarConstraints.Builder()
                .setValidator(DateValidatorPointForward.now())

        val dateRangePicker = MaterialDatePicker.Builder
            .dateRangePicker().setCalendarConstraints(constraintsBuilder.build())
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
                finish()
            }
        }

        return super.onOptionsItemSelected(item)
    }
}
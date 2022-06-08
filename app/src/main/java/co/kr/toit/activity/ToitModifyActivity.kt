package co.kr.toit.activity

import android.app.TimePickerDialog
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import android.widget.Toast
import co.kr.toit.SQLiteManager.DBHelper
import co.kr.toit.R
import co.kr.toit.databinding.ActivityToitModifyBinding
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
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
    val currentTime = System.currentTimeMillis()
    var start_date = currentTime
    var end_date = currentTime
    var start_time = myTimer.time
    var end_time = myTimer.time

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityToitModifyBinding.inflate(layoutInflater)
        setContentView(b.root)


        supportActionBar?.setDisplayShowCustomEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayShowHomeEnabled(false)

        setSupportActionBar(b.modiToolbar)
        title = "메모 편집 창"

        // 상태창 색깔 코드로 바꾸기
        if (Build.VERSION.SDK_INT >= 21) {
            val window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = this.resources.getColor(R.color.SecondaryColor)
        }


        initModiUI()

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



    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.modi_menu, menu)
        return true
    }

    // 툴바의 홈버튼 누르면 홈으로 돌아가게 하는 메서드
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> { reload() }
            R.id.modi_save -> {
                SQL_Modify_SaveManager()
                reload()
                finish()
            }
            R.id.modi_create_sub -> {
                Toast.makeText(this, "메인 프로젝트의 세부 과제를 생성합니다.", Toast.LENGTH_SHORT).show()
            }
            R.id.modi_delete_sub -> {
                Toast.makeText(this, "메인 프로젝트의 세부 과제를 삭제합니다.", Toast.LENGTH_SHORT).show()
            }
        }

        return super.onOptionsItemSelected(item)
    }
    
    private fun SQL_Modify_SaveManager(){
        val sdf1 = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val sdf2 = SimpleDateFormat("HH:mm", Locale.getDefault())
        val sdf3 = SimpleDateFormat("yyyy-MM-dd HH:mm ", Locale.getDefault())

        val startRawDate = Date(start_date)
        val startRawTime = start_time
        val endRawDate = Date(end_date)
        val endRawTime = end_time

        val StringStartDate = sdf1.format(startRawDate)
        val StringStartTime = sdf2.format(startRawTime)
        val StringEndDate = sdf1.format(endRawDate)
        val StringEndTime = sdf2.format(endRawTime)

        val StarDateArr = StringStartDate.split('-')
        val StartTimeArr = StringStartTime.split(':')
        val EndDateArr = StringEndDate.split('-')
        val EndTimeArr = StringEndTime.split(':')


        val year1 = Integer.parseInt(StarDateArr[0])
        val month1 = Integer.parseInt(StarDateArr[1])
        val day1 = Integer.parseInt(StarDateArr[2])
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

        val main_idx = intent.getIntExtra("main_idx", 0)

        val helper = DBHelper(this)
        val sql = """
                    update MainTaskTable
                    set main_task = ?, main_edit_date = ?, 
                    main_start_date = ?, main_end_date = ?
                    where main_idx = ?
                """.trimIndent()


        val arg1 = arrayOf(
            b.modiMainTaskInputText.text.toString(), sdf3.format(Date()), main_start_date,
            main_end_date, main_idx.toString()
        )

        helper.writableDatabase.execSQL(sql, arg1)
        helper.writableDatabase.close()
    }

    private fun initModiUI(){

        val helper = DBHelper(this)
        val sql = """
            select main_task, main_edit_date, main_start_date, main_end_date             
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
        val idx4 = c1.getColumnIndex("main_end_date")

        helper.writableDatabase.close()

        val MainTask = c1.getString(idx1)
        val EditDate = c1.getString(idx2)
        val startDate = c1.getString(idx3)
        val endDate = c1.getString(idx4)

        val StartDateArr = startDate.split('-', ':', ' ')
        val EndDateArr = endDate.split('-', ':', ' ')

        val year1 = Integer.parseInt(StartDateArr[0])
        val month1 = Integer.parseInt(StartDateArr[1])
        val day1 = Integer.parseInt(StartDateArr[2])
        val hour1 = Integer.parseInt(StartDateArr[3])
        val min1 = Integer.parseInt(StartDateArr[4])

        val year2 = Integer.parseInt(EndDateArr[0])
        val month2 = Integer.parseInt(EndDateArr[1])
        val day2 = Integer.parseInt(EndDateArr[2])
        val hour2 = Integer.parseInt(EndDateArr[3])
        val min2 = Integer.parseInt(EndDateArr[4])


        val CombinedStartDate1 = LocalDateTime.of(year1, month1, day1, hour1, min1)
        val CombinedStartDate2 = LocalDateTime.of(year2, month2, day2, hour2, min2)

        val StartDate = Date.from(CombinedStartDate1.atZone(ZoneId.systemDefault()).toInstant())
        val EndDate = Date.from(CombinedStartDate2.atZone(ZoneId.systemDefault()).toInstant())

        val sdf1 = SimpleDateFormat("yyyy년 MM월 dd일", Locale.KOREA)
        val sdf2 = SimpleDateFormat("a h:mm", Locale.KOREA)

        val main_start_date = sdf1.format(StartDate)
        val main_start_time = sdf2.format(StartDate)
        val main_end_date = sdf1.format(EndDate)
        val main_end_time = sdf2.format(EndDate)


        b.modiMainTaskInputText.setText(MainTask)
        b.modiEditdate.text = "최종 수정일 : $EditDate"
        b.modiInputDate1.setText(main_start_date)
        b.modiInputTime1.setText(main_start_time)
        b.modiInputDate2.setText(main_end_date)
        b.modiInputTime2.setText(main_end_time)

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
            start_date = datePicked.first
            end_date = datePicked.second
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
            start_time = myTimer.time
            b.modiInputTime1.setText(sdf.format(myTimer.time))
        } else {
            end_time = myTimer.time
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
    

    // 백버튼
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        reload()
    }
}
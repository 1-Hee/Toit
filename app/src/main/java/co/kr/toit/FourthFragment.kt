package co.kr.toit

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import co.kr.toit.databinding.FragmentFourthBinding
import com.prolificinteractive.materialcalendarview.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class FourthFragment : Fragment() {

    lateinit var mainActivity : MainActivity
    lateinit var fragmentFourthBinding: FragmentFourthBinding
    lateinit var calendar: MaterialCalendarView
    val test = arrayOf("#EFCD78", "#E11942", "#FF6200EE", "#000000", "#FF03DAC5")

    val date_list = ArrayList<String>()
    val task_count_list = ArrayList<Int>()



    override fun onAttach(context: Context) {
        super.onAttach(context)

        mainActivity = context as MainActivity
        fragmentFourthBinding = FragmentFourthBinding.inflate(layoutInflater)
        LoadSQLData()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_fourth, null)


        /*
        calendar.setOnDateChangedListener { widget, date, selected ->
            calendar.addDecorator(
                EventDecorator(mainActivity, test, date)
            )
        }
         */
        // 2020-12-01
        val calendar = view.findViewById<MaterialCalendarView>(R.id.Calendar)

        for(i in 0 until date_list.size){

            // xxxx년 xx월 xx일
            val date = date_list[i].split('년','월','일',' ')

            // CalendarDay
            val maxSize = task_count_list[i]

            val temp = CalendarDay.from(
                Integer.parseInt(date[0]), Integer.parseInt(date[2])-1, Integer.parseInt(date[4]))

            calendar.addDecorator(EventDecorator(mainActivity, CreateArray(maxSize), temp))

        }


        /*
        calendar.setOnDateChangedListener(object: OnDateSelectedListener {
            override fun onDateSelected(widget: MaterialCalendarView, date: CalendarDay, selected: Boolean) {
                calendar.addDecorator(EventDecorator(mainActivity, test, date))
                Log.d("fourthTest", date.toString())
            }
        })
         */


        return view
    }

    override fun onResume() {
        super.onResume()

    }

    // 매개변수의 사이즈를 갖는 배열을 생성하는 함수
    fun CreateArray(size:Int):Array<String>{

        var initSize = 0
        if(size > 5) initSize = 5 else initSize = size
        var colorValues = ArrayList<String>(initSize)
        val members = arrayOf("#EFCD78", "#E11942", "#FF6200EE", "#000000", "#FF03DAC5")

        for(i in 0 until initSize){
            colorValues.add(members[i])
        }


        return  (colorValues.toArray(arrayOfNulls<String>(colorValues.size)))

    }


    // 캘린더 뷰 선택시 글씨체가 커지고 색상을 바꾸는 데코레이터 클래스
    class TodayDecorator(input:CalendarDay) : DayViewDecorator {
        private var date = input

        override fun shouldDecorate(day: CalendarDay?): Boolean {
            return day?.equals(date)!!
        }

        override fun decorate(view: DayViewFacade?) {
            view?.addSpan(StyleSpan(Typeface.BOLD))
            view?.addSpan(RelativeSizeSpan(1.4f))
            view?.addSpan(ForegroundColorSpan(Color.parseColor("#1D872A")))
        }
    }

    private fun LoadSQLData(){

        //데이터베이스 오픈
        val helper = DBHelper(mainActivity)

        //쿼리문
        val sql = """
            select main_end_date, count(*) as DateCount from MainTaskTable
            group by main_end_date
        """.trimIndent()

        val c1 = helper.writableDatabase.rawQuery(sql, null)

        while (c1.moveToNext()) {
            // 컬럼 index를 가져온다
            val idx1 = c1.getColumnIndex("main_end_date")
            val idx2 = c1.getColumnIndex("DateCount")

            Log.d("fourthTest", idx1.toString())
            Log.d("fourthTest", idx2.toString())

            //데이터를 가져온다
            val end_date = c1.getString(idx1)
            val count = c1.getInt(idx2)

            // 데이터를 담는다
            date_list.add(end_date)
            task_count_list.add(count)

            helper.writableDatabase.close()

        }

    }

}
package co.kr.toit

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.util.Log
import android.util.Size
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import co.kr.toit.databinding.FragmentSecondBinding
import com.prolificinteractive.materialcalendarview.*
import java.util.*

class SecondFragment : Fragment() {

    lateinit var mainActivity : MainActivity
    lateinit var secondBinding: FragmentSecondBinding
    lateinit var calendar: MaterialCalendarView

    val date_list = ArrayList<String>()
    val task_count_list = ArrayList<Int>()



    override fun onAttach(context: Context) {
        super.onAttach(context)

        secondBinding = FragmentSecondBinding.inflate(layoutInflater)
        mainActivity = context as MainActivity
        LoadSQLData()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_second, null)

        val calendar = view.findViewById<MaterialCalendarView>(R.id.Calendar)

        for(i in date_list.indices){

            // xxxx년 xx월 xx일
            val date = date_list[i].split('-', ':', ' ')

            // CalendarDay
            val maxSize = task_count_list[i]

            val SavedDate = CalendarDay.from(
                Integer.parseInt(date[0]), Integer.parseInt(date[1])-1, Integer.parseInt(date[2]))
            calendar.addDecorator(EventDecorator(mainActivity, CreateArray(maxSize), SavedDate))

        }

        return view
    }

    override fun onResume() {
        super.onResume()

    }

    // 매개변수의 사이즈를 갖는 배열을 생성하는 함수
    fun CreateArray(size:Int):Array<String>{

        var temp2:Array<String>
        val members = arrayOf("#EFCD78", "#E11942", "#FF6200EE", "#000000", "#FF03DAC5")

        if(size>5){
            return members
        }else {
            temp2 = Array(size, {n -> members[n]})
        }

        return temp2

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
            select main_end_date, count(*) as cnt from MainTaskTable group by date(main_end_date);
        """.trimIndent()

        val c1 = helper.writableDatabase.rawQuery(sql, null)

        while (c1.moveToNext()) {
            // 컬럼 index를 가져온다
            val idx1 = c1.getColumnIndex("main_end_date")
            val idx2 = c1.getColumnIndex("cnt")

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
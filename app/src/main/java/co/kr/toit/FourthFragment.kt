package co.kr.toit

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import co.kr.toit.databinding.FragmentFourthBinding
import com.prolificinteractive.materialcalendarview.*
import com.prolificinteractive.materialcalendarview.spans.DotSpan
import java.util.*
import kotlin.collections.HashSet

class FourthFragment : Fragment() {

    lateinit var mainActivity : MainActivity
    lateinit var fragmentFourthBinding: FragmentFourthBinding
    lateinit var calendar: MaterialCalendarView
//    val test = arrayOf("#EFCD78", "#E11942", "#FF6200EE", "#000000", "#FF03DAC5")
    val test = arrayOf("#EFCD78")


    override fun onAttach(context: Context) {
        super.onAttach(context)

        mainActivity = context as MainActivity
        fragmentFourthBinding = FragmentFourthBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_fourth, null)

        val calendar = view.findViewById<MaterialCalendarView>(R.id.Calendar)

        calendar.setOnDateChangedListener(object: OnDateSelectedListener {
            override fun onDateSelected(widget: MaterialCalendarView,
                date: CalendarDay, selected: Boolean) {
//                calendar.addDecorator(TodayDecorator(date))
                calendar.addDecorator(EventDecorator(mainActivity, test, date))
            }
        })



        return view
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

//    // 캘린더 뷰에 점을 찍어주는 리스너
//    class EventDecorator(dates: Collection<CalendarDay>): DayViewDecorator {
//
//        var dates: HashSet<CalendarDay> = HashSet(dates)
//
//        override fun shouldDecorate(day: CalendarDay?): Boolean {
//            return dates.contains(day)
//        }
//
//        override fun decorate(view: DayViewFacade?) {
//            view?.addSpan(DotSpan(5F, Color.parseColor("#1D872A")))
//            view?.addSpan(DotSpan(5F, Color.parseColor("#EFCD78")))
//
//        }
//    }
}
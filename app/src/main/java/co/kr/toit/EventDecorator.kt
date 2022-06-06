package co.kr.toit

import android.content.Context
import android.graphics.Color
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade

class EventDecorator(private  val context:Context, private val stringProductColor:Array<String>,
private val dates:CalendarDay) : DayViewDecorator
{

    private lateinit var colors:IntArray

    override fun shouldDecorate(day: CalendarDay?): Boolean = dates == day


    override fun decorate(view: DayViewFacade?) {
        colors = IntArray(stringProductColor.size)
        for(i in stringProductColor.indices){
            colors[i] = Color.parseColor(stringProductColor[i])

        }
        view?.addSpan(CustomMultipleDotSpan(5f, colors))
    }

}
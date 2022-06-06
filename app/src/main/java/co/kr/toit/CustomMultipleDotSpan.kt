package co.kr.toit

import android.graphics.Canvas
import android.graphics.Paint
import android.text.style.LineBackgroundSpan
import com.prolificinteractive.materialcalendarview.spans.DotSpan.DEFAULT_RADIUS

class CustomMultipleDotSpan : LineBackgroundSpan {

    private val radius:Float
    private var color = IntArray(0)

    constructor(){
        this.radius = DEFAULT_RADIUS
        this.color[0] = 0
    }

    constructor(color:Int){
        this.radius = DEFAULT_RADIUS
        this.color[0] = 0
    }

    constructor(radius:Float){
        this.radius = radius
        this.color[0] = 0
    }

    constructor(radius: Float, color: IntArray){
        this.radius = radius
        this.color = color
    }


    override fun drawBackground(
        p0: Canvas, p1: Paint, p2: Int, p3: Int,
        p4: Int, p5: Int, p6: Int, p7: CharSequence,
        p8: Int, p9: Int, p10: Int)
    {
        val total = if(color.size > 5) 5 else color.size
        var leftMost = (total-1)*-6
        for(i in 0 until total){
            val oldColor = p1.color
            if(color[i]!=0){
                p1.color = color[i]
            }
            p0.drawCircle(((p2+p3)/2 - leftMost).toFloat(), p6+radius, radius, p1)
            p1.color = oldColor
            leftMost += 12
        }
    }

}
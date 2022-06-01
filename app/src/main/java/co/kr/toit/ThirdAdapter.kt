package co.kr.toit

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class ThirdAdapter(
    private val context : Context,
    private val dailyCount : IntArray
) : BaseAdapter(){

    private var layoutInflater: LayoutInflater? = null

    override fun getCount(): Int {
        return dailyCount.size
    }

    override fun getItem(p0: Int): Any {
        return p0
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {

        var convertView = p1
        if(layoutInflater==null){
            layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        }
        if(convertView==null){
            convertView = layoutInflater!!.inflate(R.layout.mothly_item, null)
        }

        convertView!!.findViewById<TextView>(R.id.monthly_text).text = "${dailyCount[p0]} 개"

        return convertView


    }

}
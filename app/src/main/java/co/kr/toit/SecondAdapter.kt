package co.kr.toit

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView

internal class SecondAdapter(
    private val context :Context,
    private val numberImage : IntArray
) : BaseAdapter(){

    private var layoutInflater: LayoutInflater? = null

    override fun getCount(): Int = numberImage.size

    override fun getItem(p0: Int): Any? = numberImage[p0]

    override fun getItemId(p0: Int): Long = p0.toLong()

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {

        var convertView = p1
        if(layoutInflater==null){
            layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        }
        if(convertView==null){
            convertView = layoutInflater!!.inflate(R.layout.row_item, null)
        }
        val img = convertView!!.findViewById<ImageView>(R.id.grid_img)
        img.setImageResource(numberImage[p0])

        return convertView

    }
}
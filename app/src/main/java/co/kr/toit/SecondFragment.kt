package co.kr.toit

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import co.kr.toit.databinding.FragmentSecondBinding


class SecondFragment :Fragment() {

    private val DayNameArray = arrayOf(
        "일", "월", "화", "수", "목", "금", "토"
    )

    private val CountNumberArray = intArrayOf(
        1,2,3,4,5,6,7
    )


    val subject_list = ArrayList<String>()
    val idx_list = ArrayList<Int>()
    val date2_list = ArrayList<String>()



    lateinit var mainActivity: MainActivity
    lateinit var fragmentSecondBinding: FragmentSecondBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)

        mainActivity = context as MainActivity
        fragmentSecondBinding = FragmentSecondBinding.inflate(layoutInflater)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_second, null)
        val gr = view.findViewById<GridView>(R.id.second_grid)
        gr.adapter = SecondAdapter(mainActivity, DayNameArray, CountNumberArray)



        val spinner: Spinner = view.findViewById(R.id.second_spinner)
        ArrayAdapter.createFromResource(
            mainActivity,
            R.array.planets_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }


        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                Toast.makeText(mainActivity, "$p2 번째 누름", Toast.LENGTH_SHORT).show()


            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }



        return view
    }


    override fun onResume() {
        super.onResume()

        LoadSQLData()


    }

    private fun LoadSQLData(){
        //데이터베이스 오픈
        val helper = DBHelper(mainActivity)

        //쿼리문
        val sql = """
            select rec_subject, rec_idx, rec_date2
            from Recordtable
            order by rec_idx
        """.trimIndent()

        val c1 = helper.writableDatabase.rawQuery(sql, null)
        while (c1.moveToNext()) {
            // 컬럼 index를 가져온다
            val idx1 = c1.getColumnIndex("rec_subject")
            val idx2 = c1.getColumnIndex("rec_idx")
            val idx3 = c1.getColumnIndex("rec_date2")

            //데이터를 가져온다
            val rec_subject = c1.getString(idx1)
            val rec_idx = c1.getInt(idx2)
            val rec_date2 = c1.getString(idx3)

            // 데이터를 담는다
            subject_list.add(rec_subject)
            idx_list.add(rec_idx)
            date2_list.add(rec_date2)

        }

    }


}
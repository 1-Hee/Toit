package co.kr.toit

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import co.kr.toit.databinding.FragmentSecondBinding
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.temporal.WeekFields
import java.util.*
import kotlin.collections.ArrayList


class SecondFragment :Fragment() {

    private val DayNameArray = arrayOf(
        "일", "월", "화", "수", "목", "금", "토"
    )

    private val CountNumberArray = intArrayOf(
        1,2,3,4,5,6,7
    )

    private val CountDailyArray = intArrayOf(
        1,2,3,4,5,6,7,
        1,2,3,4,5,6,7,
        1,2,3,4,5,6,7,
        1,2,3,4,5,6,7,
        1,2,3,4,5,6,7
    )


    val subject_list = ArrayList<String>()
    val idx_list = ArrayList<Int>()
    val date2_list = ArrayList<String>()

    // 현재 주차를 구함
    val CurrentDate : LocalDate = LocalDate.now()
    val weekFields: WeekFields = WeekFields.of(Locale.getDefault())
    val weekNumber: Int = CurrentDate.get(weekFields.weekOfWeekBasedYear())

    // 이번주의 날짜들을 담을 배열
    val testArray = ArrayList<LocalDate>()

    // 현재 주차를 기준으로 일주일 간의 날짜를 구하고 배열에 저장?

    lateinit var mainActivity: MainActivity
    lateinit var fragmentSecondBinding: FragmentSecondBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)


        var answer = CurrentDate.get(weekFields.weekOfWeekBasedYear())
        var current = CurrentDate

        while(answer==weekNumber){
            val temp = current.minusDays(1)
            answer = temp.get(weekFields.weekOfWeekBasedYear())
            if(weekNumber == answer){
                current = temp
            }
        }
        answer = current.get(weekFields.weekOfWeekBasedYear())

        while(answer==weekNumber){
            testArray.add(current)
            current = current.plusDays(1)
            answer = current.get(weekFields.weekOfWeekBasedYear())
        }

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

        val spinner: Spinner = view.findViewById(R.id.second_spinner)
        ArrayAdapter.createFromResource(
            mainActivity,
            R.array.SecondSpinner,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                when(p2){
                    0 -> {
                        gr.numColumns = 1
                        gr.adapter = SecondAdapter(mainActivity, DayNameArray, CountNumberArray)
                    }
                    1 -> {
                        gr.numColumns = 7
                        gr.adapter = ThirdAdapter(mainActivity, CountDailyArray)
                    }
                }
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


    }


}
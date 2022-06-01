package co.kr.toit

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import android.widget.Toast
import androidx.fragment.app.Fragment
import co.kr.toit.databinding.FragmentSecondBinding


class SecondFragment :Fragment() {

    private val DayNameArray = arrayOf(
        "일", "월", "화", "수", "목", "금", "토"
    )

    private val CountNumberArray = intArrayOf(
        1,2,3,4,5,6,7
    )



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
        gr.setOnItemClickListener { adapterView, view, i, l ->
            Toast.makeText(mainActivity, "$i 번째 누름", Toast.LENGTH_SHORT).show()
        }



        return view
    }

}
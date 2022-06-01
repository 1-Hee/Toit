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
import co.kr.toit.databinding.FragmentThirdBinding

class ThirdFragment: Fragment() {

    lateinit var mainActivity: MainActivity
    lateinit var fragmentThirdBinding: FragmentThirdBinding

    private var Images = intArrayOf(R.drawable.a1,R.drawable.a2,R.drawable.a3,R.drawable.a4,
        R.drawable.a5,R.drawable.a6,R.drawable.a7,
        R.drawable.a1,R.drawable.a2,R.drawable.a3,R.drawable.a4,
        R.drawable.a5,R.drawable.a6,R.drawable.a7,
        R.drawable.a1,R.drawable.a2,R.drawable.a3,R.drawable.a4,
        R.drawable.a5,R.drawable.a6,R.drawable.a7,
        R.drawable.a1,R.drawable.a2,R.drawable.a3,R.drawable.a4,
        R.drawable.a5,R.drawable.a6,R.drawable.a7,
        R.drawable.a1,R.drawable.a2,R.drawable.a3,R.drawable.a4,
        R.drawable.a5,R.drawable.a6,R.drawable.a7)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
        fragmentThirdBinding = FragmentThirdBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_third, null)
        val gr = view.findViewById<GridView>(R.id.third_grid)
        gr.adapter = SecondAdapter(mainActivity, Images)
        gr.setOnItemClickListener { adapterView, view, i, l ->
            Toast.makeText(mainActivity, "$i 번째 누름", Toast.LENGTH_SHORT).show()
        }
        return view
    }
}

package co.kr.toit

import android.content.Context
import android.media.Image
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.GridView
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.kr.toit.databinding.FragmentSecondBinding


class SecondFragment :Fragment() {

    private var Images = intArrayOf(R.drawable.a1,R.drawable.a2,R.drawable.a3,R.drawable.a4,
        R.drawable.a5,R.drawable.a6,R.drawable.a7)

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
        gr.adapter = SecondAdapter(mainActivity, Images)
        gr.setOnItemClickListener { adapterView, view, i, l ->
            Toast.makeText(mainActivity, "$i 번째 누름", Toast.LENGTH_SHORT).show()
        }



        return view
    }

}
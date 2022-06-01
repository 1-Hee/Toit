package co.kr.toit

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import co.kr.toit.databinding.FragmentMainBinding
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
        Log.d("test", "OnAttach")

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Log.d("test", "OnCreatView")

        val view = inflater.inflate(R.layout.fragment_second, null)

        return view
    }


    // 프래그먼트가 생성될때 생성
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("test", "OnCreate")
    }



    // 프래그먼트를 통해 보여줄 View가 생성되면 호출(after)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("test", "OnViewCreated")

    }

    // 액티비티에서 보여줄 프래그먼트가 완전히 생성되면 호출
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d("test", "OnActivityCreated")

    }

    // 프래그먼트가 화면헤 표시될때 호출됨(프래그먼트가 가동될 때 호출)
    override fun onStart() {
        super.onStart()
        Log.d("test", "OnStart")
    }

    // 프래그먼트가 보여지고 나서 호출
    override fun onResume() {
        super.onResume()
        Log.d("test", "OnResume")

        val secondAdapter = SecondAdapter(mainActivity, Images)
        fragmentSecondBinding.secondGrid.adapter = secondAdapter


    }

    //프래그먼트가 일시 정지될때 호출(프래그먼트가 화면에서 사라질때)
    override fun onPause() {
        super.onPause()
        Log.d("test", "OnPause")

    }

    // 프래그먼트가 화면에서 완전히 사라져서 더이상 보여지지 않을때 호출(프래그먼트 정지)
    override fun onStop() {
        super.onStop()
        Log.d("test", "OnStop")
    }

    // 프래그먼트가 제거될 때 호출, 메모리에서 제거 시(액티비티 종료시)
    override fun onDestroy() {
        super.onDestroy()
        Log.d("test", "OnDestroy")
    }

    // 프래그먼트가 액티비티와 연결이 완전히 끊기기 전에 호출
    override fun onDetach() {
        super.onDetach()
        Log.d("test", "OnDetach")
    }
}
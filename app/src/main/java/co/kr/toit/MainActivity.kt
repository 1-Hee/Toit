package co.kr.toit

import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.kr.toit.databinding.ActivityMainBinding
import co.kr.toit.databinding.FragmentMainBinding
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.timer

class MainActivity : AppCompatActivity() {

    lateinit var b : ActivityMainBinding
    private var TimerTask : Timer? = null

    val fg1 = MainFragment()
    val fg2 = SecondFragment()
    val fg3 = ThirdFragment()

    var position = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)

        setSupportActionBar(b.mainToolbar)
        title = ""

        b.mainFragBtn.setOnClickListener {
            val tran = supportFragmentManager.beginTransaction()
            tran.replace(R.id.main_frame, fg1)
            tran.commit()
            position = 0
        }

        b.secondFragBtn.setOnClickListener {
            val tran = supportFragmentManager.beginTransaction()
            tran.replace(R.id.main_frame, fg2)
            tran.commit()
            position = 1
        }

        b.thirdFragBtn.setOnClickListener {
            val tran = supportFragmentManager.beginTransaction()
            tran.replace(R.id.main_frame, fg3)
            tran.commit()
            position = 2
        }

        // 타이머 있던 곳,
        // 타이머는 없애야 할듯, 타이머 작동하는 동안에
        // 프래그먼트의 재구성이 전혀 안되서 버벅거리는 듯하고, 업데이트가 안됨 (나중에 시간지나서 되긴 하는데 너무 느림)


        // 문제점
        // 현재 시스템의 흐름
        // 메인 액티비티 -> 프래그먼트1 -> 서브 액티비티 -> 메인 액티비티


    }

    override fun onResume() {
        super.onResume()
        val tran = supportFragmentManager.beginTransaction()
        tran.replace(R.id.main_frame, fg1)
        tran.commit()
    }

}
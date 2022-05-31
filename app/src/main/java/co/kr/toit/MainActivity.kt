package co.kr.toit

import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import co.kr.toit.databinding.ActivityMainBinding
import co.kr.toit.databinding.FragmentMainBinding
import com.google.android.material.tabs.TabLayoutMediator
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.timer

class MainActivity : AppCompatActivity() {

    lateinit var b : ActivityMainBinding

    val fg1 = MainFragment()
    val fg2 = SecondFragment()
    val fg3 = ThirdFragment()
    val fragment_list = arrayOf(fg1, fg2, fg3)
    val fragment_title = arrayOf("일일 스케줄", "월간 스케줄", "나의 통계")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)

        setSupportActionBar(b.mainToolbar)
        title = resources.getString(R.string.app_name)

        val viewPagerAdapter = object : FragmentStateAdapter(this){
            override fun getItemCount(): Int {
                return fragment_list.size
            }

            override fun createFragment(position: Int): Fragment {
                return fragment_list[position]
            }
        }

        b.mainContainer.adapter = viewPagerAdapter

        TabLayoutMediator(b.mainTab, b.mainContainer) { tab, position ->
            tab.text = fragment_title[position]
        }.attach()


    }

    override fun onStop() {
        super.onStop()
        Log.d("test_app", "OnStop")
        finish()
    }


}
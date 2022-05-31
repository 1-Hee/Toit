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
    var restart = true;

    var position = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)

        Log.d("test_app", "OnCreate")

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

    }

    override fun onResume() {
        super.onResume()

        val tran = supportFragmentManager.beginTransaction()
        tran.replace(R.id.main_frame, fg1)
        tran.commit()
        position = 0


        Log.d("test_app", "OnResume")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d("test_app", "OnRestart")
    }

    override fun onStop() {
        super.onStop()
        Log.d("test_app", "OnStop")
        finish()
    }


}
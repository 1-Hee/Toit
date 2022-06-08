package co.kr.toit.activity


import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import co.kr.toit.*
import co.kr.toit.SQLiteManager.DBHelper
import co.kr.toit.databinding.ActivityMainBinding
import co.kr.toit.databinding.FragmentMainBinding
import co.kr.toit.fragment.MainFragment
import co.kr.toit.fragment.SecondFragment
import co.kr.toit.fragment.ThirdFragment
import com.google.android.material.tabs.TabLayoutMediator
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {

    lateinit var b : ActivityMainBinding
    lateinit var r : FragmentMainBinding


    val fg1 = MainFragment()
    val fg2 = SecondFragment()
    val fg3 = ThirdFragment()
    var fragment_list = arrayOf(fg1, fg2, fg3)
    val fragment_title = arrayOf("일일 스케줄", "월간 스케줄", "나의 통계")
    var deleteIndex = ArrayList<Int>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityMainBinding.inflate(layoutInflater)
        r = FragmentMainBinding.inflate(layoutInflater)
        setContentView(b.root)

        supportActionBar?.setDisplayShowCustomEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayShowHomeEnabled(false)

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

    override fun onResume() {
        super.onResume()
        deleteIndex.clear()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.item1 -> { ShowDeleteDialog() }
            R.id.item2 -> { showClearSubject() }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onStop() {
        super.onStop()
        finish()
    }

    fun showClearSubject(){
        var builder = AlertDialog.Builder(this)
        builder.setTitle("미션 저장")
        builder.setMessage("미션 클리어 했는가?")
        builder.setPositiveButton("저장"){dialogInterface, i ->
            if(deleteIndex.size>0) {
                for (i in deleteIndex.indices) {
                    val helper = DBHelper(this)
                    val index = deleteIndex[i]
                    val sql = """
                    update MainTaskTable
                    set completion_flag = ?
                    where main_idx = ?
                """.trimIndent()


                    val arg1 = arrayOf("true", index.toString())

                    helper.writableDatabase.execSQL(sql, arg1)
                    helper.writableDatabase.close()

                    // r.mainFragRecycler.adapter?.notifyDataSetChanged()
                }

                    overridePendingTransition(0, 0)//인텐트 효과 없애기
                    finish()
                    val newMainActivity = Intent(this, MainActivity::class.java)
                    overridePendingTransition(0, 0)//인텐트 효과 없애기
                    startActivity(newMainActivity)


            }else Toast.makeText(this, "완료할 항목이 없습니다", Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("취소",null)
        builder.show()

    }

    fun ShowDeleteDialog(){
        var builder = AlertDialog.Builder(this)
        builder.setTitle("To it List 삭제")
        builder.setMessage("정말로 할 일을 삭제하시겠습니까?")
        val Array = Array(deleteIndex.size, {n -> deleteIndex[n].toString()})

        builder.setPositiveButton("삭제"){ dialogInterface, i ->
            //데이터 베이스 오픈
            if(deleteIndex.size>0){
                for(i in deleteIndex.indices){
                    val index = deleteIndex[i]
                    val helper = DBHelper(this)
                    //쿼리문
                    val sql = """
                    delete from MainTaskTable where main_idx = ?
                    """.trimIndent()

                    //쿼리문 실행
                    val args = arrayOf(index.toString())
                    helper.writableDatabase.execSQL(sql, args)
                    helper.writableDatabase.close()

//                    r.mainFragRecycler.adapter?.notifyDataSetChanged()

                }

                finish()
                overridePendingTransition(0, 0);//인텐트 효과 없애기
                val newMainActivity = Intent(this, MainActivity::class.java)
                startActivity(newMainActivity)
            }else Toast.makeText(this, "삭제할 항목이 없습니다", Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("취소",null)
        builder.show()

    }



}
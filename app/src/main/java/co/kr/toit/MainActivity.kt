package co.kr.toit

import android.content.Intent
import android.icu.number.IntegerWidth
import android.net.MailTo.parse
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.format.Time
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IntegerRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.kr.toit.databinding.ActivityMainBinding
import co.kr.toit.databinding.MainRecyclerRowBinding
import java.net.HttpCookie.parse
import java.text.DateFormat
import java.text.Format
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoField
import java.time.temporal.ChronoUnit
import java.util.*
import java.util.Date.parse
import java.util.logging.Level.parse
import kotlin.collections.ArrayList
import kotlin.concurrent.timer

class MainActivity : AppCompatActivity() {

    lateinit var b : ActivityMainBinding

    val subject_list = ArrayList<String>()
    val idx_list = ArrayList<Int>()
    val date2_list = ArrayList<String>()
    val time2_list = ArrayList<String>()

    private var TimerTask : Timer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)

        title = "To Do List"

        //Recycler View 셋팅
        val main_recycler_adapter = MainRecylcerAdapter()
        b.memoRecycler.adapter = main_recycler_adapter
        b.memoRecycler.layoutManager = LinearLayoutManager(this)

        b.mainFb1.setOnClickListener {
            val memo_add_intent = Intent(this, ToitAddActivity::class.java)
            startActivity(memo_add_intent)
        }

        TimerTask = timer(period = 60000){

            runOnUiThread {
                b.memoRecycler.adapter?.notifyDataSetChanged()
            }
        }



    }

    override fun onResume() {
        super.onResume()

        // ArrayList를 비워준다 why? 안비워주면 다시 돌아올때마다 누적되기 때문
        subject_list.clear()
        idx_list.clear()
        date2_list.clear()
        time2_list.clear()

        //데이터베이스 오픈
        val helper = DBHelper(this)

        //쿼리문
        val sql = """
            select rec_subject, rec_idx, rec_date2, rec_time2
            from Recordtable
            order by rec_idx desc
        """.trimIndent()

        val c1 = helper.writableDatabase.rawQuery(sql, null)
        while (c1.moveToNext()) {
            // 컬럼 index를 가져온다
            val idx1 = c1.getColumnIndex("rec_subject")
            val idx2 = c1.getColumnIndex("rec_idx")
            val idx3 = c1.getColumnIndex("rec_date2")
            val idx4 = c1.getColumnIndex("rec_time2")

            //데이터를 가져온다
            val rec_subject = c1.getString(idx1)
            val rec_idx = c1.getInt(idx2)
            val rec_date2 = c1.getString(idx3)
            val rec_time2 = c1.getString(idx4)

            // 데이터를 담는다
            subject_list.add(rec_subject)
            idx_list.add(rec_idx)
            date2_list.add(rec_date2)
            time2_list.add(rec_time2)

            //RecyclerView에가 갱신하라고 함
            b.memoRecycler.adapter?.notifyDataSetChanged()
        }

    }


    // Recycler의 어댑터
    inner class MainRecylcerAdapter : RecyclerView.Adapter<MainRecylcerAdapter.ViewHolderClass>(){

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
            val mainRecyclerBinding = MainRecyclerRowBinding.inflate(layoutInflater)
            val holder = ViewHolderClass(mainRecyclerBinding)

            // 생성되는 항목 View의 가로 세로 길이를 설정
            val layoutParams = RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            mainRecyclerBinding.root.layoutParams = layoutParams

            // 항목 View에 이벤트를 설정
            mainRecyclerBinding.root.setOnClickListener(holder)

            return holder
        }

        override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {

            holder.recSubject.text = subject_list[position]

            val long_now = System.currentTimeMillis()
            val TransDate = Date(long_now)
            val DateFormat1 = SimpleDateFormat("a h:mm", Locale("ko", "KR"))
            val DateFormat2 = SimpleDateFormat("yyyy년 MM월 dd일", Locale("ko", "KR"))
            val TimeValue = DateFormat1.format(TransDate).toString()
            val DateValue = DateFormat2.format(TransDate).toString()

            // 최종 표시될 텍스트를 받을 변수
            var result = ""

            // 시간 차이를 담을 변수
            var Hdiff = 0
            var Mdiff = 0

            // 시간 값들을 담을 변수
            var temp1 = ""
            var temp2 = ""
            var temp3 = ""
            var temp4 = ""


            if(DateValue.equals(date2_list[position])){// 날짜가 같은지 필터링
                // 시간에 대해서만 연산 필요. 시간이 같은지는 연산을 통해 값으로 필터링
                //오전 3:00 의 형식 3 56
                //오전 12:00 의 형식 34 67
                var Judge = true

                if(TimeValue[1].equals(time2_list[position][1])){ // 오전 오후가 같은지
                    if(TimeValue.length == time2_list[position].length){ // 둘의 자릿수가 같은지

                        if(TimeValue.length == 8){
                            temp1 = time2_list[position][3].toString()+time2_list[position][4].toString()
                            temp2 = TimeValue[3].toString()+TimeValue[4].toString()
                            Hdiff = Integer.parseInt(temp1) - Integer.parseInt(temp2)

                            temp3 = time2_list[position][6].toString()+time2_list[position][7].toString()
                            temp4 = TimeValue[6].toString()+TimeValue[7].toString()
                            Mdiff = Integer.parseInt(temp3) - Integer.parseInt(temp4)

                        }else {
                            temp1 = time2_list[position][3].toString()
                            temp2 = TimeValue[3].toString()
                            Hdiff = Integer.parseInt(temp1) - Integer.parseInt(temp2)

                            temp3 = time2_list[position][5].toString()+time2_list[position][6].toString()
                            temp4 = TimeValue[5].toString()+TimeValue[6].toString()
                            Mdiff = Integer.parseInt(temp3) - Integer.parseInt(temp4)
                        }

                    }else if(TimeValue.length>time2_list[position].length){
                        Judge = false
                    } else {
                        // time2_list의 값, 즉 sql 값이 크니까 시간이 남은 것으로 시간 계산
                        val temp = time2_list[position][3].toString()+time2_list[position][4].toString()
                        Hdiff = Integer.parseInt(temp)-Integer.parseInt(TimeValue[3].toString())

                        temp3 = time2_list[position][5].toString()+time2_list[position][6].toString()
                        temp4 = TimeValue[5].toString()+TimeValue[6].toString()
                        Mdiff = Integer.parseInt(temp3) - Integer.parseInt(temp4)
                    }

                } else{
                    if(TimeValue[1].toString().equals("후")){
                    // 여기서는 현재시간 : 오후 vs 저장시간 : 오전 / 현재시간 : 오전 vs 저장시간 : 오후
                    // 두 가지 경우의 수만 들어오는데 전자는 시간 오버임 (날짜도 같으므로)
                        result = "기한만료"
                    }else {
                        // 현재시간 오전 vs 저장시간 후
                        if(TimeValue.length == time2_list[position].length){
                            temp1 = time2_list[position][3].toString()+time2_list[position][4].toString()
                            temp2 = TimeValue[3].toString()+TimeValue[4].toString()
                            Hdiff = Integer.parseInt(temp1)-Integer.parseInt(temp2)+12

                            temp3 = time2_list[position][6].toString()+time2_list[position][7].toString()
                            temp4 = TimeValue[6].toString()+TimeValue[7].toString()
                            Mdiff = Integer.parseInt(temp3)-Integer.parseInt(temp4)

                        }else if (TimeValue.length > time2_list[position].length){
                            temp1 = time2_list[position][3].toString()
                            temp2 = TimeValue[3].toString()+TimeValue[4].toString()
                            Hdiff = Integer.parseInt(temp1)-Integer.parseInt(temp2)+12

                            temp3 = time2_list[position][5].toString()+time2_list[position][6].toString()
                            temp4 = TimeValue[6].toString()+TimeValue[7].toString()
                            Mdiff = Integer.parseInt(temp3)-Integer.parseInt(temp4)

                        }else {
                            temp1 = time2_list[position][3].toString()+time2_list[position][4].toString()
                            temp2 = TimeValue[3].toString()
                            Hdiff = Integer.parseInt(temp1)-Integer.parseInt(temp2)+12

                            temp3 = time2_list[position][6].toString()+time2_list[position][7].toString()
                            temp4 = TimeValue[5].toString()+TimeValue[6].toString()
                            Mdiff = Integer.parseInt(temp3)-Integer.parseInt(temp4)
                        }
                    }
                }
                if(Judge){
                    if(Hdiff>0 && Mdiff < 0){
                        Mdiff += 60
                        Hdiff -= 1
                    }

                    if(Mdiff<0){
                        result = "기한 만료"
                    }else {
                        if(Hdiff>0){
                            result = "${Hdiff}시간 ${Mdiff}분"
                        }else {
                            result = "${Mdiff}분 남음"
                        }
                    }

                }else {
                    result = "기한 만료"
                }

            } else {
                // 현재보다 date2_list의 값이 큰지 작은지 대소 구분 필요.
                result = "날짜가 다르네요"
                // DateValue.toString() + date2_list[position]
            }


            holder.mainTimer.text = result
        }

        override fun getItemCount(): Int {
            return subject_list.size
        }


        // HolderClass
        inner class ViewHolderClass(mainRecyclerBinding: MainRecyclerRowBinding) : RecyclerView.ViewHolder(mainRecyclerBinding.root), View.OnClickListener{
            //view의 주소값을 담는다.
            val recSubject = mainRecyclerBinding.recSubject
            val mainIndiCater = mainRecyclerBinding.mainIndicator
            val mainTimer = mainRecyclerBinding.mainDeadline

            override fun onClick(p0: View?) {
                val rec_idx = idx_list[adapterPosition]

                val memoModifyActivity = Intent(baseContext, ToitModifyActivity::class.java)
                memoModifyActivity.putExtra("rec_idx", rec_idx)
                startActivity(memoModifyActivity)
            }


        }

    }

    override fun onStop() {
        super.onStop()
        TimerTask?.cancel()

    }

}
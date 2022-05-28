package co.kr.toit

import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.IntegerRes
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.kr.toit.databinding.ActivityMainBinding
import co.kr.toit.databinding.MainRecyclerRowBinding
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.timer

class MainActivity : AppCompatActivity() {

    lateinit var b : ActivityMainBinding

    val subject_list = ArrayList<String>()
    val idx_list = ArrayList<Int>()
    val date2_list = ArrayList<String>()
    val time2_list = ArrayList<String>()
    var StarIdx = 0.0f
    var vis = false

    private var TimerTask : Timer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)

        setSupportActionBar(b.mainToolbar)
        title = ""


        //Recycler View 셋팅
        val main_recycler_adapter = MainRecylcerAdapter()
        b.memoRecycler.adapter = main_recycler_adapter
        b.memoRecycler.layoutManager = LinearLayoutManager(this)

        b.mainFb1.setOnClickListener {
            val memo_add_intent = Intent(this, ToitAddActivity::class.java)
            startActivity(memo_add_intent)
        }
        b.mainFb1.setOnLongClickListener {
            if(vis){
                vis = false
                b.memoRecycler.adapter?.notifyDataSetChanged()
            } else {
                vis = true
                b.memoRecycler.adapter?.notifyDataSetChanged()
            }
            return@setOnLongClickListener(true)
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

            // timeList를 변수에 담아서 함수에 전달
            // dateList를 변수에 담아서 함수에 전달

            val long_now = System.currentTimeMillis()
            val TransDate = Date(long_now)
            val DateFormat1 = SimpleDateFormat("a h:mm", Locale("ko", "KR"))
            val DateFormat2 = SimpleDateFormat("yyyy년 MM월 dd일", Locale("ko", "KR"))
            val TimeValue = DateFormat1.format(TransDate).toString()
            val DateValue = DateFormat2.format(TransDate).toString()

            // 최종 표시될 텍스트를 받을 변수
            var FinalText = ""

            if(DateValue.equals(date2_list[position])){// 날짜가 같은지 필터링
                FinalText = newCalculateTime(time2_list[position], TimeValue)
            } else {
                FinalText = CalculateDate(date2_list[position], DateValue)
            }

            holder.mainTimer.text = FinalText
            holder.mainIndiCater.rating = StarIdx

            if(vis){
                holder.checkbox.visibility = View.VISIBLE
            }else {
                holder.checkbox.visibility = View.GONE
            }



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
            val checkbox = mainRecyclerBinding.mainCheckbox

            override fun onClick(p0: View?) {
                val rec_idx = idx_list[adapterPosition]

                val memoModifyActivity = Intent(baseContext, ToitModifyActivity::class.java)
                memoModifyActivity.putExtra("rec_idx", rec_idx)
                startActivity(memoModifyActivity)
            }

        }
    }

    fun CalculateDate(SavedDate:String, CurrentDate:String):String{

        var tempY1 = ""
        var tempY2 = ""
        var tempM1 = ""
        var tempM2 = ""
        var tempD1 = ""
        var tempD2 = ""
        var result = ""

        //date2_list
        tempY1 = SavedDate[0].toString()+SavedDate[1].toString()+
                SavedDate[2].toString()+SavedDate[3].toString()
        tempY2 = CurrentDate[0].toString() + CurrentDate[1].toString() +
                CurrentDate[2].toString()+CurrentDate[3].toString()

        tempM1 = SavedDate[6].toString()+SavedDate[7].toString()
        tempM2 = CurrentDate[6].toString() + CurrentDate[7].toString()

        tempD1 = SavedDate[10].toString()+SavedDate[11].toString()
        tempD2 = CurrentDate[10].toString() + CurrentDate[11].toString()

        val dateParse1 = LocalDate.parse(tempY1+"-"+tempM1+"-"+tempD1)
        val dateParse2 = LocalDate.parse(tempY2+"-"+tempM2+"-"+tempD2)

        val RawM = dateParse2.until(dateParse1, ChronoUnit.MONTHS)
        val RawD = dateParse2.until(dateParse1, ChronoUnit.DAYS)

        val diffY = dateParse2.until(dateParse1, ChronoUnit.YEARS)
        val diffM = RawM%12
        val diffD = RawD%365

        if(RawM>0) StarIdx = 3.0f
        else if(RawD>13) StarIdx = 2.5f
        else if(RawD>6) StarIdx = 2.0f
        else if(RawD>2) StarIdx = 1.5f
        else if(RawD>0) StarIdx = 1.0f

        if(diffY>0 && diffM.equals(0)){
            result = "${diffY}년 남음"
        }else if(diffY >0){
            result = "${diffY}년 ${diffM}개월 남음"
        }else if (diffM>0 && diffD.equals(0)){
            result = "${diffM}개월 남음"
        }else if (diffM > 0 && diffD > 0){
            result = "${diffM}개월 ${diffD}일 남음"
        }else if (diffD > 0){
            result = "${diffD}일 남음"
        } else {
            result = "기한 만료"
            StarIdx = 0.0f
        }

        return result

    }

    fun newCalculateTime(SavedTime: String, CurrentTime: String):String{

        var tempH1 = ""
        var tempH2 = ""
        var tempM1 = ""
        var tempM2 = ""
        var diffH = 0
        var diffM = 0

        var addIndx1 = 0
        var addIndx2 = 0

        var result = ""

        if(SavedTime[1].equals("후")){ addIndx1 = 12 }
        if(CurrentTime[1].equals("후")){ addIndx2 = 12 }

        if(SavedTime.length==8){
            tempH1 = SavedTime[3].toString() + SavedTime[4].toString()
            tempM1 = SavedTime[6].toString() + SavedTime[7].toString()
        }else {
            tempH1 = "0"+SavedTime[3].toString()
            tempM1 = SavedTime[5].toString() + SavedTime[6].toString()
        }

        if(CurrentTime.length==8){
            tempH2 = CurrentTime[3].toString() + CurrentTime[4].toString()
            tempM2 = CurrentTime[6].toString() + CurrentTime[7].toString()
        }else {
            tempH2 = "0"+CurrentTime[3].toString()
            tempM2 = CurrentTime[5].toString() + CurrentTime[6].toString()
        }

        if(addIndx1>0){
            val temp = Integer.parseInt(tempH1)+addIndx1
            tempH1 = temp.toString()
        }

        if(addIndx2>0){
            val temp = Integer.parseInt(tempH2)+addIndx2
            tempH2 = temp.toString()
        }

        diffH = Integer.parseInt(tempH1)-Integer.parseInt(tempH2)
        diffM = Integer.parseInt(tempM1)-Integer.parseInt(tempM2)

        if(diffH>0 && diffM < 0){
            diffM += 60
            diffH -= 1
        }

        if(diffM<0){
            result = "기한 만료"
            StarIdx = 0.0f

        }else {
            StarIdx = 0.5f
            if(diffH>0){
                result = "${diffH}시간 ${diffM}분"
            }else {
                result = "${diffM}분 남음"
            }
        }

        return result
    }

    override fun onStop() {
        super.onStop()
        TimerTask?.cancel()

    }

}
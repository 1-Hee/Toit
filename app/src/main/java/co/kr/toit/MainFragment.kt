package co.kr.toit

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.kr.toit.databinding.FragmentMainBinding
import co.kr.toit.databinding.MainRecyclerRowBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.collections.ArrayList

class MainFragment : Fragment() {

    val subject_list = ArrayList<String>()
    val idx_list = ArrayList<Int>()
    val date_list = ArrayList<String>()
    val time_list = ArrayList<String>()
    var StarIdx = 0.0f
    var vis = false

    lateinit var mainActivity: MainActivity
    lateinit var fragmentMainBinding: FragmentMainBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)

        mainActivity = context as MainActivity
        fragmentMainBinding = FragmentMainBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_main, null)

        // 내부의 Veiw들의 주소값을 가져온다
        val rc1 = view.findViewById<RecyclerView>(R.id.main_frag_recycler)
        val fb1 = view.findViewById<FloatingActionButton>(R.id.main_frag_fb1)

        // Recycler View 세팅
        val main_recycler_adapter = MainFragmentRecyclerAdapter()
        rc1.adapter = main_recycler_adapter
        rc1.layoutManager = LinearLayoutManager(mainActivity)


        fb1.setOnClickListener {
            val Add_intent = Intent(mainActivity, ToitAddActivity::class.java)
            startActivity(Add_intent)
        }

        fb1.setOnLongClickListener {
            if (vis) {
                vis = false
                rc1.adapter?.notifyDataSetChanged()

            } else {
                vis = true
                rc1.adapter?.notifyDataSetChanged()
            }
            return@setOnLongClickListener (true)
        }

        subject_list.clear()
        idx_list.clear()
        date_list.clear()
        time_list.clear()

        //데이터베이스 오픈
        val helper = DBHelper(mainActivity)

        //쿼리문
        val sql = """
            select main_task, main_idx, main_end_date, main_end_time
            from MainTaskTable
            order by main_idx desc
        """.trimIndent()

        val c1 = helper.writableDatabase.rawQuery(sql, null)

        while (c1.moveToNext()) {
            // 컬럼 index를 가져온다
            val idx1 = c1.getColumnIndex("main_task")
            val idx2 = c1.getColumnIndex("main_idx")
            val idx3 = c1.getColumnIndex("main_end_date")
            val idx4 = c1.getColumnIndex("main_end_time")

            //데이터를 가져온다
            val main_task = c1.getString(idx1)
            val main_idx = c1.getInt(idx2)
            val end_date = c1.getString(idx3)
            val end_time = c1.getString(idx4)

            // 데이터를 담는다
            subject_list.add(main_task)
            idx_list.add(main_idx)
            date_list.add(end_date)
            time_list.add(end_time)

            helper.writableDatabase.close()

            fragmentMainBinding.mainFragRecycler.adapter?.notifyDataSetChanged()
        }

        return view
    }

    override fun onResume() {
        super.onResume()


    }


    inner class MainFragmentRecyclerAdapter : RecyclerView.Adapter<MainFragmentRecyclerAdapter.ViewHolderClass>(){

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
            val mainRecyclerBinding = MainRecyclerRowBinding.inflate(layoutInflater)
            val holder = ViewHolderClass(mainRecyclerBinding)
            val layoutParams = RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            mainRecyclerBinding.root.layoutParams = layoutParams

            mainRecyclerBinding.root.setOnClickListener(holder)

            return holder

        }

        override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {

            holder.recSubject.text = subject_list[position]

            val long_now = System.currentTimeMillis()
            val TransDate = Date(long_now)
            val DateFormat1 = SimpleDateFormat("a h:mm", Locale("ko", "KR"))
            val DateFormat2 = SimpleDateFormat("yyyy년 MM월 dd일", Locale("ko", "KR"))
            // yyyy-mm-dd
            val TimeValue = DateFormat1.format(TransDate).toString()
            val DateValue = DateFormat2.format(TransDate).toString()

            var FinalText = ""

            if(DateValue == date_list[position]){// 날짜가 같은지 필터링
//                FinalText = newCalculateTime(time_list[position], TimeValue)
            } else {
                FinalText = CalculateDate(date_list[position], DateValue)
            }

            holder.fragTimer.text = FinalText
            holder.fragIndicator.rating = StarIdx

            if(vis){
                holder.fragcheckBox.visibility = View.VISIBLE
            }else {
                holder.fragcheckBox.visibility = View.GONE
            }


        }

        override fun getItemCount(): Int {
            return subject_list.size
        }

        inner class ViewHolderClass(mainRecyclerBinding: MainRecyclerRowBinding) : RecyclerView.ViewHolder(mainRecyclerBinding.root), View.OnClickListener{
            // View의 주소 값을 담음.
            val recSubject = mainRecyclerBinding.recSubject
            val fragIndicator = mainRecyclerBinding.mainIndicator
            val fragTimer = mainRecyclerBinding.mainDeadline
            val fragcheckBox = mainRecyclerBinding.mainCheckbox

            override fun onClick(p0: View?) {

                val main_idx = idx_list[adapterPosition]

                val toitModifyActivity = Intent(mainActivity.baseContext, ToitModifyActivity::class.java)
                toitModifyActivity.putExtra("main_idx", main_idx)
                startActivity(toitModifyActivity)
            }
        }

    }

    fun CalculateDate(SavedDate:String, CurrentDate:String):String{

        var result = ""

        val formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일")
        val date1 = LocalDate.parse(SavedDate, formatter)
        val date2 = LocalDate.parse(CurrentDate, formatter)

        val RawM = date2.until(date1, ChronoUnit.MONTHS)
        val RawD = date2.until(date1, ChronoUnit.DAYS)
        val diffY = date2.until(date1, ChronoUnit.YEARS)

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

        val SavedTimeArr = SavedTime.split(' ')
        val CurrentTimeArr = CurrentTime.split(' ')

        val trans1 = LocalTime.parse(SavedTimeArr[1])
        val trans2 = LocalTime.parse(CurrentTimeArr[1])

        var SavedHour = trans1.hour
        val SavedMin = trans1.minute

        var CurrentHour = trans2.hour
        val CurrentMin = trans2.minute

        if(SavedTimeArr[0]=="오전"){
            if(SavedHour==12){
                SavedHour = 0
            }
        }else{
            if(SavedHour!=12){
                SavedHour+=12
            }
        }

        if(CurrentTimeArr[0]=="오전"){
            if(CurrentHour==12){
                CurrentHour = 0
            }
        }else{
            if(CurrentHour!=12){
                CurrentHour+=12
            }
        }

        var diffH = SavedHour-CurrentHour
        var diffM = SavedMin - CurrentMin

        if(diffM<0){
            diffH-=1
            diffM+=60
        }

        if(diffH<0){
            StarIdx = 0.0f
            return "시간 초과"
        }else {
            StarIdx = 0.5f
            if(diffH>0){
                return "${diffH}시간 ${diffM}분"
            }else {
                return "${diffM}분 남음"
            }
        }

    }
}
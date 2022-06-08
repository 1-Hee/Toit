package co.kr.toit

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.kr.toit.databinding.MainRecyclerRowBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.collections.ArrayList

class MainFragment : Fragment() {

    val subject_list = ArrayList<String>()
    val idx_list = ArrayList<Int>()
    val date_list = ArrayList<LocalDateTime>()

    var current_time = System.currentTimeMillis()
    var TransCurrentTime = Date(current_time).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

    var TimeDiff = 0
    var vis = false

    lateinit var mainActivity: MainActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)

        mainActivity = context as MainActivity

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

        UpdateRecyclerViewManager()

        val spin = view.findViewById<Spinner>(R.id.MFspinner)
        spin.adapter = ArrayAdapter.createFromResource(mainActivity, R.array.SecondSpinner, android.R.layout.simple_spinner_dropdown_item)

        return view
    }

    private fun UpdateRecyclerViewManager(){

        subject_list.clear()
        idx_list.clear()
        date_list.clear()

        //데이터베이스 오픈
        val helper = DBHelper(mainActivity)

        //쿼리문
        val sql = """
            select main_task, main_idx, main_end_date
            from MainTaskTable
            order by main_idx desc
        """.trimIndent()

        val c1 = helper.writableDatabase.rawQuery(sql, null)

        while (c1.moveToNext()) {
            // 컬럼 index를 가져온다
            val idx1 = c1.getColumnIndex("main_task")
            val idx2 = c1.getColumnIndex("main_idx")
            val idx3 = c1.getColumnIndex("main_end_date")

            //데이터를 가져온다
            val main_task = c1.getString(idx1)
            val main_idx = c1.getInt(idx2)
            val endDate = c1.getString(idx3)

            val EndDateArr = endDate.split('-', ':', ' ')

            val year2 = Integer.parseInt(EndDateArr[0])
            val month2 = Integer.parseInt(EndDateArr[1])
            val day2 = Integer.parseInt(EndDateArr[2])
            val hour2 = Integer.parseInt(EndDateArr[3])
            val min2 = Integer.parseInt(EndDateArr[4])

            val CombinedStartDate2 = LocalDateTime.of(year2, month2, day2, hour2, min2)

            // 데이터를 담는다
            subject_list.add(main_task)
            idx_list.add(main_idx)
            date_list.add(CombinedStartDate2)

            helper.writableDatabase.close()
        }

    }

    override fun onResume() {
        super.onResume()
        // 프래그먼트 LifeCycle에 따라서 다른 프래그먼트로 이동하다가 원래로 돌아오면 Resume()상태가 됨.
        // 이를 응용하여 프래그먼트 간 이동이 있을 경우에는 새로 올때마다 시간 값을 갱신하도록 Resume()에 구현.
        current_time = System.currentTimeMillis()
        TransCurrentTime = Date(current_time).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
        UpdateRecyclerViewManager()
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

            val TimerText = newCalculateDate(date_list[position], TransCurrentTime)
            holder.fragTimer.text = TimerText
            holder.fragIndicator.rating = CaculateStars(TimeDiff)

            if(vis){
                holder.fragcheckBox.visibility = View.VISIBLE
            }else {
                holder.fragcheckBox.visibility = View.GONE
            }

            holder.fragcheckBox.setOnCheckedChangeListener { compoundButton, b ->
                if(b){
                    mainActivity.deleteIndex.add(idx_list[position])
                } else {
                    mainActivity.deleteIndex.remove(idx_list[position])
                }
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

    fun CaculateStars(diff:Int):Float{

        var Stars = 0f

        when(diff){
            3-> Stars = 3.0f
            2-> Stars = 2.0f
            1-> Stars = 1.0f
            0-> Stars = 0f
        }

        return Stars
    }



    fun newCalculateDate(SavedDate: LocalDateTime, CurrentTime: LocalDateTime):String{
        val YearDiff = CurrentTime.until(SavedDate, ChronoUnit.YEARS)
        val MonthDiff = CurrentTime.until(SavedDate, ChronoUnit.MONTHS)
        val DayDiff = CurrentTime.until(SavedDate, ChronoUnit.DAYS)
        val HourDiff = CurrentTime.until(SavedDate, ChronoUnit.HOURS)
        val MinDiff = CurrentTime.until(SavedDate, ChronoUnit.MINUTES)
        var result = ""

        TimeDiff = 3

        if(YearDiff>0){
            result = "${YearDiff}년 ${MonthDiff%12}개월"
        }else if (YearDiff==0L && MonthDiff>0){
            result = "${MonthDiff}개월 ${MonthDiff}일"
        } else if (MonthDiff==0L && DayDiff>0){
            result = "${DayDiff}일 남음"
            if(DayDiff == 2L) TimeDiff = 2 else TimeDiff = 1
        } else if( DayDiff == 0L && HourDiff> 0){
            TimeDiff = 1
            result = "${HourDiff}시간 ${MinDiff%60}분"
        } else if(MinDiff>0){
            TimeDiff = 1
            result = "${MinDiff}분 남음"
        } else {
            TimeDiff = 0
            result = "시간 초과"
        }

        return result

    }

}
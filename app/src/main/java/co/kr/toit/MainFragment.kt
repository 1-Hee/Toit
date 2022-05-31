package co.kr.toit

import android.content.Context
import android.content.Intent
import android.icu.number.IntegerWidth
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
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.collections.ArrayList

class MainFragment : Fragment() {

    val subject_list = ArrayList<String>()
    val idx_list = ArrayList<Int>()
    val date2_list = ArrayList<String>()
    val time2_list = ArrayList<String>()
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
            val memo_add_intent = Intent(mainActivity, ToitAddActivity::class.java)
            startActivity(memo_add_intent)
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
        date2_list.clear()
        time2_list.clear()

        //데이터베이스 오픈
        val helper = DBHelper(mainActivity)

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
            val TimeValue = DateFormat1.format(TransDate).toString()
            val DateValue = DateFormat2.format(TransDate).toString()

            var FinalText = ""

            if(DateValue == date2_list[position]){// 날짜가 같은지 필터링
                FinalText = newCalculateTime(time2_list[position], TimeValue)
            } else {
                FinalText = CalculateDate(date2_list[position], DateValue)
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

                val rec_idx = idx_list[adapterPosition]

                val memoModifyActivity = Intent(mainActivity.baseContext, ToitModifyActivity::class.java)
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

        var result = ""
        StarIdx = 1.0f

        // 가져온 시간을 먼저 숫자로 변환 변환
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

        if(SavedTime[1].equals("후")&&!tempH1[1].equals("2")){
            tempH1 = (Integer.parseInt(tempH1)+12).toString()
        }

        if(CurrentTime[1].equals("후")&&!tempH2[1].equals("2")){
            tempH2 = (Integer.parseInt(tempH2)+12).toString()
        }

        diffH = Integer.parseInt(tempH1)-Integer.parseInt(tempH2)
        diffM = Integer.parseInt(tempM1)-Integer.parseInt(tempM2)

        if(diffH>=0){
            if(diffM<0) {
                diffM += 60
                diffH -= 1
            }

            if(diffH>=0){
                if(diffH!=0){
                    result = "${diffH}시간 ${diffM}분"
                } else {
                    result = "${diffM}분 남음"
                }

            } else {
                result = "기한 만료"
                StarIdx = 0.0f
            }

        }else {
            result = "기한 만료"
            StarIdx = 0.0f
        }

        return result
    }
}
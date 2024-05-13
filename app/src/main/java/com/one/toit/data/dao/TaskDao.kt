package com.one.toit.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.one.toit.data.model.Task
import java.util.Date

@Dao
interface TaskDao {
    /**
     * 전체 조회
     */
    // TODO 정렬 옵션(생성 날짜), 페이징 추가하기
    @Query("SELECT * FROM table_task_registration " +
            "INNER JOIN table_task_information" +
            " ON task_id = fk_task_id")
    fun readTaskList(): List<Task>
    @Query("SELECT * FROM table_task_registration " +
            "INNER JOIN table_task_information " +
            "ON task_id = fk_task_id "+
            "LIMIT :size OFFSET (:page-1)*:size")
    fun readTaskList(page:Int, size:Int): List<Task> // 페이징 추가한 메서드

    @Query("SELECT * FROM table_task_registration " +
            "INNER JOIN table_task_information " +
            "ON task_id = fk_task_id " +
            "WHERE task_title LIKE '%'||:query||'%' "
    )
    fun readTaskListByQuery(query:String): List<Task> // 검색

    @Query("SELECT * FROM table_task_registration " +
            "INNER JOIN table_task_information " +
            "ON task_id = fk_task_id " +
            "WHERE task_title LIKE '%'||:query||'%' " +
            "LIMIT :size OFFSET (:page-1)*:size")
    fun readTaskListByQuery(page:Int, size:Int, query:String): List<Task> // 페이징 & 검색
    @Query("SELECT * FROM table_task_registration " +
            "INNER JOIN table_task_information " +
            "ON task_id = fk_task_id WHERE COALESCE(task_complete, '') = ''")
    fun readRemainTaskList(): List<Task>

    @Query("SELECT * FROM table_task_registration " +
            "INNER JOIN table_task_information " +
            "ON task_id = fk_task_id WHERE COALESCE(task_complete, '') = '' " +
            "LIMIT :size OFFSET(:page-1)*:size")
    fun readRemainTaskList(page:Int, size:Int): List<Task> // 페이징 추가한 메서드

    /**
     *  일일 조회
     */
    @Query("SELECT * FROM table_task_registration a " +
            "INNER JOIN table_task_information " +
            "ON task_id = fk_task_id " +
            "WHERE DATE(a.create_at) = DATE(:targetDate) ")
    fun readTaskListByDate(targetDate: Date): List<Task>

    @Query("SELECT * FROM table_task_registration a " +
            "INNER JOIN table_task_information " +
            "ON task_id = fk_task_id " +
            "WHERE DATE(a.create_at) = DATE(:targetDate) " +
            "LIMIT :size OFFSET(:page-1)*:size")
    fun readTaskListByDate(page: Int, size:Int, targetDate: Date): List<Task> // 페이징 추가

    @Query("SELECT * FROM table_task_registration a " +
            "INNER JOIN table_task_information " +
            "ON task_id = fk_task_id " +
            "WHERE COALESCE(task_complete, '') = '' " +
            "AND DATE(a.create_at) = DATE(:targetDate)")
    fun readRemainTaskListByDate(targetDate: Date): List<Task>
    // readRemainTaskList(page:Int)

    @Query("SELECT * FROM table_task_registration a " +
            "INNER JOIN table_task_information " +
            "ON task_id = fk_task_id " +
            "WHERE COALESCE(task_complete, '') = '' " +
            "AND DATE(a.create_at) = DATE(:targetDate)" +
            "LIMIT :size OFFSET(:page-1)*:size")
    fun readRemainTaskListByDate(page:Int, size:Int, targetDate: Date): List<Task> // 페이징 추가

    // 통계 관련...
    /**
     *  시간대별 달성 추이 확인
     */

    /**
     * 주간 통계 관련!
     */
    // 일일 task 개수 카운트 함수
    @Query("SELECT COUNT(ti.info_id) FROM table_task_registration tr " +
            "INNER JOIN table_task_information ti " +
            "ON task_id = fk_task_id " +
            "WHERE DATE(tr.create_at) = DATE(:targetDate) ")
    fun getTaskCntByDate(targetDate: Date): Int

    // 일일 완료 task 개수 카운트 함수
    // 카운트 계수
    @Query("SELECT COUNT(ti.info_id) FROM table_task_registration tr " +
            "INNER JOIN table_task_information ti " +
            "ON task_id = fk_task_id " +
            "WHERE COALESCE(task_complete, '') = '' " +
            "AND DATE(tr.create_at) = DATE(:targetDate) ")
    fun getRemainTaskCntByDate(targetDate: Date): Int

    @Query("SELECT COUNT(task_id) FROM table_task_registration a " +
            "INNER JOIN table_task_information " +
            "ON task_id = fk_task_id " +
            "WHERE COALESCE(task_complete, '') != '' " +
            "AND DATE(a.create_at) = DATE(:targetDate)")
    fun getCompleteTaskCnt(targetDate: Date):Int


    /**
     * 전체 통계 관련
     */

    // 전체 목표 수
    @Query("SELECT COUNT(tr.task_id) FROM table_task_registration tr " +
            "INNER JOIN table_task_information ti " +
            "ON task_id = fk_task_id ")
    fun getAllTaskCnt(): Long

    // 평균 목표 수
    @Query("SELECT AVG(cnt) " +
            "FROM(" +
            "   SELECT COUNT(tr.task_id) as cnt " +
            "   FROM table_task_registration tr " +
            "   INNER JOIN table_task_information ti " +
            "   ON task_id = fk_task_id " +
            "   GROUP BY DATE(tr.create_at)" +
            ") as sub_table;")
    fun getAvgTaskCnt(): Float
    // 월간 목표 수
    @Query("SELECT COUNT(tr.task_id) FROM table_task_registration tr " +
            "INNER JOIN table_task_information ti " +
            "ON task_id = fk_task_id " +
            "WHERE strftime('YYYY-MM', tr.create_at) = strftime('YYYY-MM', :mDate)")
    fun getMonthTaskCnt(mDate: Date): Long

    // 최장 기록
    @Query("SELECT MAX(" +
            "   CASE WHEN task_complete IS NOT NULL " +
            "       THEN (strftime('%s', task_complete) - strftime('%s', create_at)) " +
            "   ELSE null END)" +
            "FROM table_task_registration tr " +
            "INNER JOIN table_task_information ti " +
            "ON task_id = fk_task_id;"
    )
    fun getMaxTaskTime(): Long
    // 최단 기록
    @Query("SELECT MIN(" +
            "   CASE WHEN task_complete IS NOT NULL " +
            "       THEN (strftime('%s', task_complete) - strftime('%s', create_at)) " +
            "   ELSE null END)" +
            "FROM table_task_registration tr " +
            "INNER JOIN table_task_information ti " +
            "ON task_id = fk_task_id;"
    )
    fun getMinTaskTime(): Long
    // 평균 기록
    @Query("SELECT AVG(" +
            "   CASE WHEN(strftime('%s', task_complete) - strftime('%s', create_at)) > 0 " +
            "   THEN (strftime('%s', task_complete) - strftime('%s', create_at)) " +
            "   ELSE null END" +
            ") FROM table_task_registration tr " +
            "INNER JOIN table_task_information ti " +
            "ON task_id = fk_task_id;")
    fun getAvgTaskTime():Float

    /**
     *  점수 산정을 위한 메서드
     */

    // 메모 점수 산정을 위한 메모 길이
    @Query(
        "SELECT LENGTH(ti.task_memo) " +
                "FROM table_task_information ti " +
                "WHERE ti.fk_task_id = :fkTaskId;"
    )
    fun getMemoLength(fkTaskId:Long):Int

    /**
     * 달성 시간 구하는 메서드
     * CASE 문을 통해 달성 시간이 없을 경우 - 값이 나오도록 함!
     */
    @Query(
        "SELECT (CASE " +
                "   WHEN task_limit IS NOT NULL " + // 제한이 있으면,
                "   THEN (strftime('%s', task_complete) - strftime('%s', create_at)) " +
                "   ELSE -1 END) as complete_time " +
                "FROM table_task_registration " +
                "INNER JOIN table_task_information " +
                "ON task_id = fk_task_id " +
                "WHERE fk_task_id = :fkTaskId; "
    )
    fun getCompleteTime(fkTaskId: Long):Long

    /**
     * 주어진 시간대 까지의 목표 달성 기록을 체크
     */
    @Query("SELECT COUNT(a.task_id) FROM table_task_registration a " +
            "INNER JOIN table_task_information " +
            "ON task_id = fk_task_id " +
            "WHERE DATE(a.create_at) = DATE(:targetDate) " +
            "AND strftime('%H', task_complete) <= strftime('%H', :targetDate)")
    fun getAchievementCnt(targetDate: Date):Int

}
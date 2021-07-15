package com.lk.myproject.utils

import android.util.Log
import com.lk.myproject.activity.Util
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

inline fun log(msg: String) {
    Log.d("lk###", msg)
}

fun main() {
    getTime(2)
}

var beginTime: Long = 0
var endTime: Long = 0

private val sdf = SimpleDateFormat("yyyy-MM-dd")
fun getThisWeekInterval(): Pair<String, String> {
    val date = Date()
    val cal = Calendar.getInstance()
    cal.time = date
    val dayWeek = cal[Calendar.DAY_OF_WEEK] // 获得当前日期是一个星期的第几天
    if (1 == dayWeek) {
        cal.add(Calendar.DAY_OF_MONTH, -1)
    }
    cal.firstDayOfWeek = Calendar.MONDAY
    val day = cal[Calendar.DAY_OF_WEEK]
    cal.add(Calendar.DATE, cal.firstDayOfWeek - day)
    val beginDate = sdf.format(cal.time)
    cal.add(Calendar.DATE, 6)
    val endDate = sdf.format(cal.time)
    return Pair(beginDate, endDate)
}

fun getTime(type: Int) {
    endTime = System.currentTimeMillis()
    var car = Calendar.getInstance()
    car.timeInMillis = endTime
    var day = car.get(Calendar.DAY_OF_WEEK)
    println("day = $day")

    var temp = Util.getThisWeekInterval()
    println("this temp = $temp")

    getThisWeekInterval()?.let {
        println("this week = ${it.first},${it.second}")
    }

    temp = Util.getLastWeekInterval()
    println("last temp = $temp")


    temp = Util.getThisMonthInterval()
    println("this month temp = $temp")

    temp = Util.getLastMonthInterval()
    println("last month temp = $temp")

    val sdf2 = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    var t = sdf2.parse("2021-07-09 00:00:00")
    println("t = ${t}")
    println("t = ${t.time}")
}
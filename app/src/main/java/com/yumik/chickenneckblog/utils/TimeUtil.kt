package com.yumik.chickenneckblog.utils

import java.util.*

fun Long.formatTime(): String {
    val calendar = Calendar.getInstance()
    val time =(calendar.timeInMillis - this) /1000
//    Log.d("formatTime", "timeInMillis:${calendar.timeInMillis},this:${this},time:${time},")
    return when {
        time in 3..59 ->
            "${time}秒前";
        time in 60..3599 ->
            "${time / 60}分钟前"
        time in 3600..86399 ->
            "${time / 3600}小时前"
        time in 86400..2591999 ->
            "${time / 3600 / 24}天前"
        time in 2592000..31103999 ->
            "${time / 3600 / 24 / 30}个月前"
        time >= 31104000 -> "${time / 3600 / 24 / 30 / 12}年前"
        else -> "刚刚"
    }
}
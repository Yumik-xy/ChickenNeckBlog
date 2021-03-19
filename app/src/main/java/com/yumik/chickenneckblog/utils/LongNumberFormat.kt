package com.yumik.chickenneckblog.utils

import com.yumik.chickenneckblog.utils.LongNumberFormat.format

object LongNumberFormat {
    fun Long.format(): String {
        return when {
            this < 10000 -> "$this"
            this in 10000..1000000 -> "${(this / 10000.0).toString().format("%0.1f")}万"
            this in 1000000..100000000 -> "${(this / 1000000.0).toString().format("%0.1f")}百万"
            else -> "∞"
        }
    }
}

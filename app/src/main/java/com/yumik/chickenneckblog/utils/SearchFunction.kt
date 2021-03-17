package com.yumik.chickenneckblog.utils

import com.yumik.chickenneckblog.R

object SearchFunction {
    private val classifyMap: LinkedHashMap<Int, Int> = LinkedHashMap()
    private val sortMap: LinkedHashMap<String, Int> = LinkedHashMap()

    fun getClassify(layout: Int): Int {
        val num = classifyMap[layout]
        return num ?: 0
    }

    fun getSortOrder(sort: String): Int {
        val num = sortMap[sort]
        return num ?: 0
    }

    fun swapSortOrder(sort: String): String {
        return if (getSortOrder(sort) == 0b0) "时间排序" else "默认排序"
    }

    init {
        classifyMap[R.id.filter_title] = 0b0001
        classifyMap[R.id.filter_author] = 0b0010
        classifyMap[R.id.filter_container] = 0b0100
        classifyMap[R.id.filter_classify] = 0b1000


        sortMap["默认排序"] = 0b0
        sortMap["时间排序"] = 0b1
    }
}
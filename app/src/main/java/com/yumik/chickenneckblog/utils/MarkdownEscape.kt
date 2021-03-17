package com.yumik.chickenneckblog.utils

object MarkdownEscape {
    fun String.escape(): String {
        val out = StringBuilder(this.length + 128)
        for (i in this.indices) {
            when (val c = this[i]) {
                '"', '\\', '/' -> out.append('\\').append(c)
                '\t' -> out.append("\\t")
                '\b' -> out.append("\\b")
                '\n' -> out.append("\\n")
                '\r' -> out.append("\\r")
                else -> if (c.toInt() <= 0x1F) {
                    out.append(String.format("\\u%04x", c.toInt()))
                } else {
                    out.append(c)
                }
            }
        }
        return out.toString()
    }
}
package com.yumik.chickenneckblog.utils

import android.view.View
import java.util.*

abstract class OnUnShakeClickListener : View.OnClickListener {

    companion object {
        private const val DELAY_TIME = 1000
    }

    private var lastClickTime: Long = 0

    override fun onClick(v: View) {
        val nowClickTime: Long = Date().time
        if (nowClickTime - lastClickTime > DELAY_TIME) {
            lastClickTime = nowClickTime
            onSharkClick(v)
        }
    }

    abstract fun onSharkClick(v: View)
}

fun View.setOnUnShakeClickListener(block: (View) -> Unit) {
    this.setOnClickListener(object : OnUnShakeClickListener() {
        override fun onSharkClick(v: View) {
            block(v)
        }
    })
}
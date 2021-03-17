package com.yumik.chickenneckblog.utils

import android.content.Context
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

object TipsUtil {
    fun View.showSnackbar(
        text: String,
        actionText: String? = null,
        duration: Int = Snackbar.LENGTH_SHORT,
        block: (() -> Unit)? = null
    ) {
        val snackbar = Snackbar.make(this, text, duration)
//        snackbar.set
        if (actionText != null && block != null) {
            snackbar.setAction(actionText) {
                block()
            }
        }
        snackbar.show()
    }

    fun String.showToast(context: Context, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(context, this, duration).show()
    }
}
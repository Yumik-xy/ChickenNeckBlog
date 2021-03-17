package com.yumik.chickenneckblog.utils

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.view.ViewGroup
import android.view.WindowManager

object ImmersiveUtil {
    fun immersive(activity: Activity) {
        if (Build.VERSION.SDK_INT > +Build.VERSION_CODES.LOLLIPOP) {
            activity.window.apply {
                clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
                statusBarColor = Color.TRANSPARENT
            }
            activity.findViewById<ViewGroup>(android.R.id.content).apply {
                for (index in 0 until childCount) {
                    val child = getChildAt(index) as? ViewGroup
                    child?.let {
                        it.fitsSystemWindows = true
                        it.clipToPadding = true
                    }
                }
            }
        } else {
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
    }
}
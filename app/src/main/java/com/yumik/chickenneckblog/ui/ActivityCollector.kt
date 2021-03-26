package com.yumik.chickenneckblog.ui

import android.app.Activity

object ActivityCollector {

    private val activities = ArrayList<Activity>()

    fun Activity.addActivity() {
        activities.add(this)
    }

    fun Activity.removeActivity() {
        activities.remove(this)
    }

    fun finishAllA() {
        for (activity in activities) {
            if (!activity.isFinishing) {
                activity.finish()
            }
        }
        activities.clear()
    }
}
package com.yumik.chickenneckblog.utils

import android.content.Context
import android.content.pm.PackageManager


object ApkVersionCodeUtils {

    fun getVersionCode(context: Context): Long {
        var versionCode = 0L
        try {
            versionCode = context.packageManager
                .getPackageInfo(context.packageName, 0).longVersionCode
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return versionCode
    }

    fun getVerName(context: Context): String {
        var verName = ""
        try {
            verName =
                context.packageManager.getPackageInfo(context.packageName, 0).versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return verName
    }
}
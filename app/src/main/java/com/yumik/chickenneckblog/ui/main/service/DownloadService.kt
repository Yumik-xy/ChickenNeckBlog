package com.yumik.chickenneckblog.ui.main.service

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.yumik.chickenneckblog.R
import com.yumik.chickenneckblog.utils.TipsUtil.showToast
import com.yumik.chickenneckblog.utils.downLoad.DownloadListener
import com.yumik.chickenneckblog.utils.downLoad.DownloadUtil
import java.io.File

class DownloadService : Service() {

    private lateinit var notificationManager: NotificationManager
    private lateinit var notification: NotificationCompat.Builder


    override fun onCreate() {
        Log.d("DownloadService", "onCreate")

        super.onCreate()
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notification = NotificationCompat.Builder(this, "normal").apply {
            setTicker("开始更新")
            setContentTitle("版本更新")
            setContentText("新版本下载中")
            setSmallIcon(R.drawable.ic_logo)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val url = intent?.getStringExtra("url")
        val path = intent?.getStringExtra("path")
        if (url == null || path == null) {
            "未获取到url".showToast(this)
            return flags
        }
        DownloadUtil().download(url, path, object : DownloadListener {
            override fun onStart() {
                Log.d("DownloadUtil", "onStart")
            }

            override fun onProgress(progress: Int, currentLength: Int, totalLength: Long) {
                Log.d("DownloadUtil", "onProgress $progress%")
                notification.setProgress(100, progress, false)
                    .setContentText("新版本下载中... 已完成 ${currentLength}/${totalLength}")
                notificationManager.notify(0, notification.build())
            }

            override fun onFinish(path: String?) {
                Thread {
                    Log.d("DownloadUtil", "onFinish $path")
                    notificationManager.cancel(0)
                    if (path != null)
                        File(path).also { file ->
                            if (file.exists()) {
                                DownloadUtil().installApp(
                                    this@DownloadService,
                                    "com.yumik.chickenneckblog.fileProvider",
                                    path
                                )
                            }
                        }
                    stopSelf()
                }.start()
            }

            override fun onFail(errorInfo: String?) {
                Log.d("DownloadUtil", "onFail $errorInfo")
                notificationManager.cancel(0)
                "下载失败 $errorInfo".showToast(this@DownloadService)
                stopSelf()
            }
        })

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent): IBinder {
        TODO()
    }
}
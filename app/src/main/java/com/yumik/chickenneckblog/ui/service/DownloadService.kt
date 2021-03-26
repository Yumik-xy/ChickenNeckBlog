package com.yumik.chickenneckblog.ui.service

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

    companion object {
        private const val TAG = "DownloadService"
    }

    override fun onCreate() {
        Log.d(TAG, "onCreate")

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
        val md5 = intent?.getStringExtra("md5")
        val important = intent?.getBooleanExtra("important", false)
        if (url == null || path == null || md5 == null && important == null) {
            return flags
        }
        File(path).also { file ->
            if (file.exists() && DownloadUtil().getFileMD5(file) == md5) {
                DownloadUtil().installApp(
                    this@DownloadService,
                    "com.yumik.chickenneckblog.fileProvider",
                    path
                )
                if (important == true){

                }
                    return flags
            }
        }

        DownloadUtil().download(url, path, object : DownloadListener {
            override fun onStart() {
                Log.d("DownloadUtil", "onStart")
            }

            override fun onProgress(progress: Int, speed: Float) {
                Log.d("DownloadUtil", "onProgress $progress%")
                notification.setProgress(100, progress, false)
                    .setContentText(
                        "已完成${progress}% - ${
                            (speed / 1024).toString().format("%.2f")
                        }KB/s"
                    )
                notificationManager.notify(0, notification.build())
            }

            override fun onFinish(path: String?) {
                Thread {
                    Log.d("DownloadUtil", "onFinish $path")
                    notificationManager.cancel(0)
                    File(path!!).also { file ->
                        if (file.exists()) {
                            if (DownloadUtil().getFileMD5(file) == md5) {
                                DownloadUtil().installApp(
                                    this@DownloadService,
                                    "com.yumik.chickenneckblog.fileProvider",
                                    path
                                )
                            } else {
                                "安装包校验失败！".showToast(this@DownloadService)
                                deleteFile(path)
                            }
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
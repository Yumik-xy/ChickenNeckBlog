package com.yumik.chickenneckblog.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.yumik.chickenneckblog.ui.ActivityCollector.addActivity
import com.yumik.chickenneckblog.ui.ActivityCollector.removeActivity
import com.yumik.chickenneckblog.ui.main.MainActivity

open class BaseActivity : AppCompatActivity() {

    lateinit var receiver: ForceOfflineReceiver

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        this.addActivity()
    }

    override fun onResume() {
        super.onResume()
        val intentFilter = IntentFilter()
        intentFilter.addAction("com.yumik.chickenneckblog.FORCE_OFFLINE")
        receiver = ForceOfflineReceiver()
        registerReceiver(receiver, intentFilter)
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(receiver)
    }

    override fun onDestroy() {
        super.onDestroy()
        this.removeActivity()
    }

    inner class ForceOfflineReceiver : BroadcastReceiver() {
//        掉起使用：
//        val intent = Intent("com.yumik.chickenneckblog.FORCE_OFFLINE")
//        sendBroadcast(intent)

        override fun onReceive(context: Context, intent: Intent?) {
            AlertDialog.Builder(context).apply {
                setTitle("警告")
                setMessage("你的账号在其他地方被登录了！如果不是本人操作，请及时修改密码！")
                setCancelable(false)
                setPositiveButton("我知道了") { _, _ ->
                    ActivityCollector.finishAllA()
                    val i = Intent(context, MainActivity::class.java)
                    context.startActivity(i)
                }
                show()
            }
        }

    }
}
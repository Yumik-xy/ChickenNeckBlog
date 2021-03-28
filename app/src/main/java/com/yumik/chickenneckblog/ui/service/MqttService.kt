package com.yumik.chickenneckblog.ui.service

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.yumik.chickenneckblog.ProjectApplication
import com.yumik.chickenneckblog.R
import com.yumik.chickenneckblog.ui.main.MainActivity
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*


class MqttService : Service() {

    companion object {
        private const val TAG = "MqttService"
    }

    private val publishTopic = "androidPublishTopic"
    private val serverUri = ProjectApplication.MQTT_SERVICE
    private val topicList = listOf("mqtt/messageNotice")

    private lateinit var mqttAndroidClient: MqttAndroidClient
    private lateinit var notificationManager: NotificationManager
    private lateinit var keepServiceNotification: Notification

    class MqttBinder : Binder() {
        val service: MqttService
            get() = MqttService()
    }

    override fun onBind(intent: Intent): IBinder {
//        startForeground(1, keepServiceNotification)
        return MqttBinder()
    }

    override fun onCreate() {
        super.onCreate()
        mqttAndroidClient = MqttAndroidClient(this, serverUri, ProjectApplication.uuid)
        mqttAndroidClient.setCallback(object : MqttCallbackExtended {
            override fun connectionLost(cause: Throwable?) {
                Log.d(TAG, "connectionLost: connection was lost");
            }

            override fun messageArrived(topic: String?, message: MqttMessage?) {
                val payload = message?.let { String(it.payload) }
                Log.d(TAG, "Topic: $topic ==> Payload: $payload");
                Log.d(TAG, "Topic: $topic ==> Qos: ${message?.qos}")
                if (!::notificationManager.isInitialized) {
                    notificationManager =
                        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                }
                notificationManager.notify(
                    (Math.random() * 3000).toInt() + 1,
                    NotificationCompat.Builder(this@MqttService, "message_notice")
                        .setAutoCancel(true)
                        .setContentTitle("$payload")
                        .setContentText("$message")
                        .setSmallIcon(R.drawable.ic_logo)
                        .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_logo))
                        .setGroup("message_notice")
                        .build()
                )

            }

            override fun deliveryComplete(token: IMqttDeliveryToken?) {

            }

            override fun connectComplete(reconnect: Boolean, serverURI: String?) {
                if (reconnect) {
                    Log.d(TAG, "connectComplete: $serverURI");
                    // Because Clean Session is true, we need to re-subscribe
                    subscribeAllTopics();
                } else {
                    Log.d(TAG, "connectComplete: $serverURI");
                }
            }
        })
        val mqttConnectOptions = MqttConnectOptions().apply {
            userName = ProjectApplication.MQTT_USER
            password = ProjectApplication.MQTT_PASSWORD.toCharArray()
            isAutomaticReconnect = true // 自动重连
            isCleanSession = false //连接时不清除session缓存
            connectionTimeout = 20 // 超时20s
            keepAliveInterval = 5 // 心跳包20s
            maxInflight = 10 // 最多建立10个连接
            mqttVersion = MqttConnectOptions.MQTT_VERSION_3_1_1
        }
        try {
            Log.d(TAG, "onCreate: Connecting to $serverUri");
            mqttAndroidClient.connect(mqttConnectOptions, null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    Log.d(TAG, "onSuccess: Success to connect to $serverUri")
                    val disconnectedBufferOptions = DisconnectedBufferOptions().apply {
                        isBufferEnabled = true
                        bufferSize = 100
                        isPersistBuffer = false
                        isDeleteOldestMessages = false
                    }
                    mqttAndroidClient.setBufferOpts(disconnectedBufferOptions)
                    subscribeAllTopics()
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    Log.d(TAG, "onFailure: Failed to connect to $serverUri");
                    exception?.printStackTrace();
                }

            })
        } catch (e: MqttException) {
            e.printStackTrace()
        }
        val intent = Intent(this, MainActivity::class.java)
        val pi = PendingIntent.getActivity(this, 0, intent, 0)
        keepServiceNotification = NotificationCompat.Builder(this, "normal")
            .setContentTitle("鸡腿博客")
            .setContentText("鸡腿博客正在后台运行")
            .setSmallIcon(R.drawable.ic_logo)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_logo))
            .setContentIntent(pi)
            .setAutoCancel(true)
            .setVisibility(NotificationCompat.VISIBILITY_SECRET)
            .build()
    }

    //TODO service绑定notification

    /**
     * 订阅所有消息
     */
    fun subscribeAllTopics() {
        for (item in topicList) {
            subscribeToTopic(item)
        }
    }

    /**
     * 订阅消息
     */
    private fun subscribeToTopic(subscriptionTopic: String) {
        try {
            mqttAndroidClient.subscribe(subscriptionTopic, 2, null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken) {
                    Log.d(TAG, "onSuccess: Success to $subscriptionTopic!")
//                    publishMessage("Hello this is ${ProjectApplication.uuid}")
                }

                override fun onFailure(asyncActionToken: IMqttToken, exception: Throwable) {
                    Log.d(TAG, "onFailure: Failed to $subscriptionTopic!")
                }
            })
        } catch (e: MqttException) {
            Log.d(TAG, "subscribeToTopic: Exception whilst subscribing")
            e.printStackTrace()
        }
    }

    /**
     * 发布消息
     */
    fun publishMessage(msg: String) {
        try {
            val message = MqttMessage()
            message.payload = msg.toByteArray()
            mqttAndroidClient.publish(publishTopic, message)
            Log.d(TAG, "publishMessage: Message Published: $msg")
        } catch (e: MqttException) {
            Log.d(TAG, "publishMessage: Error Publishing: " + e.message)
            e.printStackTrace()
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "MqttService onStartCommand executed")
//        startForeground(1, keepServiceNotification)
        return super.onStartCommand(intent, START_FLAG_RETRY, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        notificationManager.cancelAll()
        stopForeground(true)
        try {
            if (::mqttAndroidClient.isInitialized) {
                mqttAndroidClient.disconnect()
            }
        } catch (e: MqttException) {
            e.printStackTrace()
        }
        Log.d(TAG, "MqttService onDestroy executed")
    }
}
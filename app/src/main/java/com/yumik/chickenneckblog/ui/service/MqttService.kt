package com.yumik.chickenneckblog.ui.service

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.yumik.chickenneckblog.ProjectApplication
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

    override fun onBind(intent: Intent): IBinder {
        throw UnsupportedOperationException("Not yet implemented")
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
                if (!::notificationManager.isInitialized) {
                    notificationManager =
                        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                }
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
            isAutomaticReconnect = true // 自动重连
            isCleanSession = false //连接时不清除session缓存
            connectionTimeout = 20 // 超时20s
            keepAliveInterval = 20 // 心跳包10s
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
        Log.d(TAG, "MqttService onStartCommand executed");
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
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
package com.gmail.pavlovsv93.whattoseetoday.service.fb

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.gmail.pavlovsv93.whattoseetoday.R
import com.google.firebase.iid.internal.FirebaseInstanceIdInternal
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

open class FirebaseService : FirebaseMessagingService() {

    companion object {
        private const val TAG_FBS = "FirebaseService.Token"
        private const val PUSH_KEY_TITLE = "title"
        private const val PUSH_KEY_MESSAGE = "message"
        private const val CHANNEL_ID = "channel_id"
        private const val NOTIFICATION_ID = 37
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        sendRegistrationToServer(token)
    }

    private fun sendRegistrationToServer(token: String) {
        Log.d(TAG_FBS, "token: ${token}")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val remoteMessageData = remoteMessage.data
        if (remoteMessageData.isNotEmpty()) {
            handlerDataMessage(remoteMessageData.toMap())
        }
    }

    private fun handlerDataMessage(dataMap: Map<String, String>) {
        val title = dataMap[PUSH_KEY_TITLE]
        val message = dataMap[PUSH_KEY_MESSAGE]
        if (!title.isNullOrBlank() && !message.isNullOrBlank()) {
            showNotification(title, message)
        }
    }

    private fun showNotification(title: String, message: String) {

        val notificationBuilder = NotificationCompat.Builder(applicationContext, CHANNEL_ID).apply {
            setSmallIcon(R.drawable.ic_movie_play)
            setContentTitle(title)
            setContentText(message)
            priority = NotificationCompat.PRIORITY_DEFAULT
        }

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

    //Если Android выше версии O, то необходимо создать канал нотификации
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val name = "Channel message firebase"
        val descriptionText = "Channel description"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        notificationManager.createNotificationChannel(channel)
    }

    override fun onDeletedMessages() {
        super.onDeletedMessages()
    }
}
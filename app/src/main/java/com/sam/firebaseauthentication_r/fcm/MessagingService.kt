package com.sam.firebaseauthentication_r.fcm

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.sam.firebaseauthentication_r.R
import com.sam.firebaseauthentication_r.utils.Constants
import kotlin.random.Random

class MessagingService: FirebaseMessagingService() {
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel(this)
        Log.d("TAG", "${this::class.simpleName} created")
    }
    @Suppress("DEPRECATION")
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("TAG", "onNewToken: $token")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val title = message.notification?.title
        val body = message.notification?.body

        Log.d("TAG", "onMessageReceived: $title $body")
        if (title != null && body != null) {
            showNotification(title, body)
        }
    }

    private fun Context.showNotification(title: String, body: String) {
        val notification: Notification =
            NotificationCompat.Builder(this, Constants.NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(body)
                .build()

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("TAG", "Notification permission is not available.")
            return
        }

        Log.d("TAG", "Showing notification")

        NotificationManagerCompat
            .from(this)
            .notify(Random.nextInt(), notification)
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                Constants.NOTIFICATION_CHANNEL_ID,
                "General Notification",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "This is a channel for showing general notification"
            }

            val notificationManager =
                context.getSystemService(NotificationManager::class.java) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }
}
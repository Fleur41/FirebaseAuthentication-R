package com.sam.firebaseauthentication_r

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.sam.firebaseauthentication_r.utils.Constants
import com.sam.firebaseauthentication_r.fcm.MessagingService
import com.sam.firebaseauthentication_r.settings.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    val settingsViewModel  by viewModels<SettingsViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val startDestination by settingsViewModel.startDestination.collectAsState()
            FirebaseApp(startDestination = startDestination)
        }
        retrieveFCMToken()
        val messagingService = Intent(this, MessagingService::class.java)
        startService(messagingService)
    }

    fun createNotificationChannel(context: Context) {
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

    fun retrieveFCMToken(){
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.d("TAG", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result
            Log.d("TAG", "retrieveFCMToken: $token")
        }
        )
    }

}


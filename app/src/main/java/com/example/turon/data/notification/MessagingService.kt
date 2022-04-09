package com.example.turon.data.notification

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.turon.R
import com.example.turon.SplashActivity
import com.example.turon.utils.SharedPref
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class MessagingService : FirebaseMessagingService() {
    private val TAG = "MyFirebaseMessagingServ"

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("UnspecifiedImmutableFlag")
    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)
        val sharedPref by lazy { SharedPref(this) }
        var notifyIntent = Intent()
        Log.d(TAG, "onMessageReceived: ${p0.notification?.title}")
        Log.d(TAG, "onMessageReceived: ${p0.notification?.body}")
        Log.d(TAG, "onMessageReceived: ${p0.data}")

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        notifyIntent = Intent(this, SplashActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val notifyPendingIntent = PendingIntent.getActivity(
            this, 0, notifyIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(this, "channelId")
        builder.setSmallIcon(R.drawable.ic_baseline_notifications_active_24)
        builder.setContentTitle(p0.data["title"])
        builder.setContentText(p0.data["body"])
        builder.setContentIntent(notifyPendingIntent)
        builder.priority = NotificationCompat.PRIORITY_MAX
        builder.setCategory(NotificationCompat.CATEGORY_MESSAGE)
        builder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
        builder.setVibrate(longArrayOf(1000, 1000, 1000, 1000))
        builder.setAutoCancel(true)
        val notification = builder.build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val mChannel = NotificationChannel(
                "channelId",
                "this is push notification",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(mChannel)
        }
        notificationManager.notify(1, notification)
    }
}
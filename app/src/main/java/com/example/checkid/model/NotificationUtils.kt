package com.example.checkid.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager

object NotificationUtils {
    private const val CHANNEL_ID = "default_channel"

    // 알림 채널 생성
    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Default Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    // 알림 전송
    fun sendUsageAlertNotification(context: Context, message: String) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_alert) // 기본 알림 아이콘
            .setContentTitle("사용 시간 경고")
            .setContentText(message)
            .setAutoCancel(true) // 클릭 시 알림이 자동 제거됨
            .build()

        notificationManager.notify(1, notification)
    }

}

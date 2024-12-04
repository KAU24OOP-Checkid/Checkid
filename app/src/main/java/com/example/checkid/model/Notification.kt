package com.example.checkid.model

import User
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.checkid.R
import com.example.checkid.model.NotificationRepository.addNotification
import com.example.checkid.view.MainActivity

class Notification (
    val notificationType : NotificationType = NotificationType.SYSTEM,
    val textContent : String = ""
)

{
    val textTitle : String = when (notificationType) {
        NotificationType.SYSTEM -> "시스템"
        NotificationType.REPORT -> "보고서"
        NotificationType.WARNING -> "경고"
        else -> ""
    }
}

enum class NotificationType(val value: Int) {
    SYSTEM(0),
    REPORT(1),
    WARNING(2)
    // ...
}

object NotificationChannelManager {
    const val PARENT_CHANNEL_ID = "parent_channel_id"
    const val CHILD_CHANNEL_ID = "child_channel_id"

    // private val userType : User = ParentUser("c", "1") //SharedPreferences 설정 후 수정

    fun createNotificationChannel(context: Context) {
        lateinit var channel: NotificationChannel

        /*
        // API 26 버전 이상은 'NotificationChannel' 이 반드시 필요하다.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            // 'UserType' 을 통해서 알맞는 'Channel' 을 만든다.
            if (userType is ParentUser) {
                channel = NotificationChannel(
                    PARENT_CHANNEL_ID,
                    "Parent Notification",
                    NotificationManager.IMPORTANCE_DEFAULT
                )

                channel.description = "This channel is used for parents"
            }

            else {
                channel = NotificationChannel(
                    CHILD_CHANNEL_ID,
                    "Child Notification",
                    NotificationManager.IMPORTANCE_HIGH
                )

                channel.description = "This channel is used for child"
            }

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

         */
    }

    fun sendNotification(context: Context, channelId: String, notificationType: NotificationType, textContent: String) {
        val notification: Notification = Notification(notificationType, textContent)

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("openFragment", "NotificationFragment")
        }

        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        addNotification(notification) // 'viewModelInstance' 는 어떻게 가져오지?

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(notification.textTitle)
            .setContentText(notification.textContent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT) // API 버전이 25 이하일 경우 사용
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(notificationType.ordinal, builder.build())
    }
}
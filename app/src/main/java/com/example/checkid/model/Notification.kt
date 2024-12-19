package com.example.checkid.model

import ParentUser
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
import com.example.checkid.model.UserRepository.getUserById
import com.example.checkid.view.activity.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class Notification (
    val notificationType : NotificationType = NotificationType.SYSTEM,
    val textContent : String = ""
)

{
    var textTitle : String = when (notificationType) {
        NotificationType.SYSTEM -> "시스템"
        NotificationType.REPORT -> "보고서"
        NotificationType.WARNING -> "경고"
        else -> ""
    }
}

data class NotificationDTO(
    val notificationType: String = "",
    val textContent: String = "",
    val textTitle: String = ""
)

fun NotificationDTO.toNotification(): Notification {
    return Notification(
        notificationType = NotificationType.fromValue(this.notificationType),
        textContent = this.textContent
    ).apply {
        textTitle = this@toNotification.textTitle
    }
}

enum class NotificationType(val value: Int) {
    SYSTEM(0),
    REPORT(1),
    WARNING(2);
    // ...

    companion object {
        fun fromValue(value: Int): NotificationType {
            return entries.find { it.value == value } ?: SYSTEM
        }

        fun fromValue(value: String): NotificationType {
            return when (value) {
                "REPORT" -> REPORT
                "WARNING" -> WARNING
                else -> SYSTEM
            }
        }
    }
}

object NotificationChannelManager {
    private const val PARENT_CHANNEL_ID = "parent_channel_id"
    private const val PARENT_CHANNEL_NAME = "Parent Notification"
    private const val PARENT_CHANNEL_IMPORTANCE = NotificationManager.IMPORTANCE_DEFAULT
    private const val PARENT_CHANNEL_DESCRIPTION = "This channel is used for parents"

    private const val CHILD_CHANNEL_ID = "child_channel_id"
    private const val CHILD_CHANNEL_NAME ="child Notification"
    private const val CHILD_CHANNEL_IMPORTANCE = NotificationManager.IMPORTANCE_HIGH
    private const val CHILD_CHANNEL_DESCRIPTION = "This channel is used for child"


    fun createNotificationChannel(context: Context) {
        lateinit var channel: NotificationChannel

        val userType = runBlocking {
            val id = DataStoreManager.getUserId(context)
            val user = getUserById(id)

            if (user != null)
                return@runBlocking UserRepository.getUserType(user)

            else
                return@runBlocking null
        }

        // API 26 버전 이상은 'NotificationChannel' 이 반드시 필요하다.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            // 'UserType' 을 통해서 알맞는 'Channel' 을 만든다.
            if (userType == "ParentUser") {
                channel = NotificationChannel(
                    PARENT_CHANNEL_ID,
                    PARENT_CHANNEL_NAME,
                    PARENT_CHANNEL_IMPORTANCE
                ).apply {
                    description = PARENT_CHANNEL_DESCRIPTION
                }
            }

            else {
                channel = NotificationChannel(
                    CHILD_CHANNEL_ID,
                    CHILD_CHANNEL_NAME,
                    CHILD_CHANNEL_IMPORTANCE
                ).apply {
                    description = CHILD_CHANNEL_DESCRIPTION
                }
            }

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    suspend fun sendNotification(context: Context, user: User, notification: Notification) {
        val channelId: String = if (user is ParentUser) {
            PARENT_CHANNEL_ID
        } else  {
            CHILD_CHANNEL_ID
        }

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

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(notification.textTitle)
            .setContentText(notification.textContent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT) // API 버전이 25 이하일 경우 사용
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        val notificationManager = NotificationManagerCompat.from(context)
        val notificationId = System.currentTimeMillis().toInt()

        notificationManager.notify(notificationId, builder.build())

        withContext(Dispatchers.IO) {
            addNotification(id = user.id, notification = notification)
        }
    }
}
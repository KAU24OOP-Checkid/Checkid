package com.example.checkid.model

import android.content.Context
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

object NotificationRepository {
    private const val SIZE = 20

    var notifications = ArrayList<Notification>().apply {
        add(Notification(NotificationType.REPORT, "1"))
        add(Notification(NotificationType.WARNING, "2"))
        add(Notification(NotificationType.SYSTEM, "3"))

        add(Notification(NotificationType.REPORT, "4"))
        add(Notification(NotificationType.WARNING, "5"))
        add(Notification(NotificationType.SYSTEM, "6"))

        add(Notification(NotificationType.REPORT, "7"))
        add(Notification(NotificationType.WARNING, "8"))
        add(Notification(NotificationType.SYSTEM, "9"))

        add(Notification(NotificationType.REPORT, "10"))
        add(Notification(NotificationType.WARNING, "11"))
        add(Notification(NotificationType.SYSTEM, "12"))
    }

    private const val COLLECTION = "NotificationRepository"

    private const val DOCUMENT_NOTIFICATION_TYPE = "notificationType"
    private const val DOCUMENT_TEXT_CONTENT = "textContent"
    private const val DOCUMENT_TEXT_TITLE = "textTitle"

    private const val DEFAULT_NOTIFICATION_TYPE = 0
    private const val DEFAULT_TEXT_CONTENT = ""
    private const val DEFAULT_TEXT_TITLE = ""

    suspend fun loadNotification(id: String) {
        val db = Firebase.firestore

        // 수정 필요
        try {
            val querySnapshot = db.collection(COLLECTION).get().await()

            for (document in querySnapshot.documents) {
                val notification = Notification(
                    notificationType = NotificationType.fromValue(
                        (document.getLong(DOCUMENT_NOTIFICATION_TYPE)?.toInt() ?: DEFAULT_NOTIFICATION_TYPE)
                    ),
                    textContent = document.getString(DOCUMENT_TEXT_CONTENT) ?: DEFAULT_TEXT_CONTENT
                ).apply {
                    textTitle = document.getString(DOCUMENT_TEXT_TITLE) ?: DEFAULT_TEXT_TITLE
                }
            }

        } catch(e: Exception) {
            e.printStackTrace()
        }
    }

    // 수정 필요
    suspend fun addNotification(notification: Notification, id: String) {
        val db = Firebase.firestore
        val notificationData = mapOf(
            DOCUMENT_NOTIFICATION_TYPE to notification.notificationType,
            DOCUMENT_TEXT_CONTENT to notification.textContent,
            DOCUMENT_TEXT_TITLE to notification.textTitle
        )

        db.collection(COLLECTION)
            .document(id)
    }

    suspend fun deleteNotification(index: Int) {
        val db = Firebase.firestore

        notifications.removeAt(index)
    }
}
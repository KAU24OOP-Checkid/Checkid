package com.example.checkid.model

import androidx.lifecycle.LifecycleCoroutineScope
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

object NotificationRepository {
    var notifications = ArrayList<Notification>()

    /*
    init {
        notifications.add(Notification(NotificationType.SYSTEM, "1"))
        notifications.add(Notification(NotificationType.REPORT, "2"))
        notifications.add(Notification(NotificationType.WARNING, "3"))
    }

     */

    private const val COLLECTION = "Notification"

    private const val DOCUMENT_NOTIFICATION_TYPE = "notificationType"
    private const val DOCUMENT_TEXT_CONTENT = "textContent"
    private const val DOCUMENT_TEXT_TITLE = "textTitle"

    private const val DEFAULT_NOTIFICATION_TYPE = ""
    private const val DEFAULT_TEXT_CONTENT = ""
    private const val DEFAULT_TEXT_TITLE = ""

    suspend fun loadNotification(id: String) {
        val db = Firebase.firestore

        try {
            val documentSnapshot = db.collection(COLLECTION)
                .document(id)
                .get()
                .await()

            val notificationMaps = documentSnapshot.get("notifications") as? List<Map<String, Any?>> ?: emptyList()

            notifications.clear()
            notifications.addAll(
                notificationMaps.map { map ->
                    NotificationDTO(
                        notificationType = map[DOCUMENT_NOTIFICATION_TYPE] as? String? ?: DEFAULT_NOTIFICATION_TYPE,
                        textContent = map[DOCUMENT_TEXT_CONTENT] as? String? ?: DEFAULT_TEXT_CONTENT,
                        textTitle = map[DOCUMENT_TEXT_TITLE] as? String? ?: DEFAULT_TEXT_TITLE
                    ).toNotification()
                }
            )

        } catch(e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun saveNotification(id: String) {
        val db = Firebase.firestore

        val data = mapOf(
            "notifications" to notifications
        )

        try {
            db.collection(COLLECTION)
                .document(id)
                .set(data)
                .await()
        } catch(e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun addNotification(id: String, notification: Notification) {
        notifications.add(notification)
        saveNotification(id)
    }

    suspend fun deleteNotification(id: String, index: Int) {
        notifications.removeAt(index)
        saveNotification(id)
    }
}
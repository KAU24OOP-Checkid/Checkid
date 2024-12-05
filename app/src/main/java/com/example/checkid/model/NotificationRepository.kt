package com.example.checkid.model

import android.content.Context
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore

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

    fun addNotification(notification: Notification) {
        if (notifications.size >= SIZE) notifications.removeAt(0)

        notifications.add(notification)
    }

    fun deleteNotification(index: Int) {
        notifications.removeAt(index)
    }

    fun getNotification(context: Context) {
        val db = FirebaseFirestore.getInstance()
        val userId = DataStoreManager.getUserId(context)

        val reference = db.collection("NotificationRepository").document("test")
        // test -> userId


        // 고치기 List<Map<>>
        /*
        reference.get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val notification = document.toObject(List<Notification>::class.java)

                    if (notification != null) {
                        notifications = notification
                    }
                }
            }

            .addOnFailureListener { exception ->
                Log.d("Firestore", "Failed to get notifications: ", exception)
            }
        */

    }
}
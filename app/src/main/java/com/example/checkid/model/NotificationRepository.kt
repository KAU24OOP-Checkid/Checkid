package com.example.checkid.model

object NotificationRepository {
    val notifications = ArrayList<Notification>().apply {
        add(Notification(NotificationType.REPORT, "", ""))
        add(Notification(NotificationType.WARNING, "", ""))
    }
}
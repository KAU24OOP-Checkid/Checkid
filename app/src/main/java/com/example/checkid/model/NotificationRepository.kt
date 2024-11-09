package com.example.checkid.model

object NotificationRepository {
    val notifications = ArrayList<Notification>().apply {
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
}
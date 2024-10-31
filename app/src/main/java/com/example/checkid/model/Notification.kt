package com.example.checkid.model

class Notification (
    val notificationType : NotificationType = NotificationType.SYSTEM,
    val textTitle : String = "",
    val textContent : String = ""
    )

enum class NotificationType {
    SYSTEM,
    REPORT,
    WARNING
    // ...
}
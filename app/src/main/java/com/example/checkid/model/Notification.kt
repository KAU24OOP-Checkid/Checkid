package com.example.checkid.model

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

enum class NotificationType {
    SYSTEM,
    REPORT,
    WARNING
    // ...
}
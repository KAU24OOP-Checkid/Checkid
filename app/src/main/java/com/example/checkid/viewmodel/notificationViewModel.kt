package com.example.checkid.viewmodel

import android.view.View
import com.example.checkid.model.Notification
import com.example.checkid.model.NotificationRepository.deleteNotification

class NotificationViewModel(view: View, model: Notification) {

    fun deleteNotificationInstance(index: Int) {
        // model에서 삭제
        deleteNotification(index)
    }
}
package com.example.checkid.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.checkid.model.Notification
import com.example.checkid.model.NotificationRepository
import com.example.checkid.model.NotificationRepository.deleteNotification
import com.example.checkid.model.NotificationRepository.notifications

class NotificationViewModel(): ViewModel() {
    private val _notifications = MutableLiveData<List<Notification>>()
    val notifications : LiveData<List<Notification>>
        get() = _notifications

    init {
        loadNotifications()
    }

    private fun loadNotifications() {
        _notifications.value = NotificationRepository.notifications
    }

    fun deleteNotificationInstance(index: Int) {
        deleteNotification(index)
        loadNotifications()
    }
}
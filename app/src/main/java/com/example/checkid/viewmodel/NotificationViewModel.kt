package com.example.checkid.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.checkid.model.DataStoreManager
import com.example.checkid.model.Notification
import com.example.checkid.model.NotificationRepository
import com.example.checkid.model.NotificationRepository.deleteNotification
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NotificationViewModel(context: Context): ViewModel() {
    private val _notifications = MutableLiveData<List<Notification>>()
    val notifications : LiveData<List<Notification>>
        get() = _notifications

    suspend fun loadNotifications(context: Context) {
        val id = DataStoreManager.getUserId(context)

        NotificationRepository.loadNotification(id)
        _notifications.value = NotificationRepository.notifications
    }

    fun deleteNotificationInstance(context: Context, index: Int) {
        viewModelScope.launch {
            val id = DataStoreManager.getUserId(context)
            deleteNotification(id, index)
            loadNotifications(context)
        }
    }
}

class NotificationViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotificationViewModel::class.java)) {
            return NotificationViewModel(context) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class : ${modelClass.name}")
    }
}
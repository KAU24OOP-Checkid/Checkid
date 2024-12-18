package com.example.checkid.view.application

import android.app.Application
import com.example.checkid.model.DataStoreManager
import com.example.checkid.model.NotificationRepository
import com.google.firebase.FirebaseApp
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class Checkid : Application() {
    override fun onCreate() {
        super.onCreate()

        FirebaseApp.initializeApp(this)

        /*
        GlobalScope.launch {
            NotificationRepository.saveNotification("parent")
        }

         */
    }
}
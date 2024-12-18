package com.example.checkid.view

import android.app.Application
import com.example.checkid.model.DataStoreManager
import com.google.firebase.FirebaseApp
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class Checkid : Application() {
    override fun onCreate() {
        super.onCreate()

        FirebaseApp.initializeApp(this)

        GlobalScope.launch {
            DataStoreManager.setUserId(applicationContext, "parent")
            DataStoreManager.setUserType(applicationContext, "Parent")
            DataStoreManager.setUserPartnerId(applicationContext, "")
        }

    }
}
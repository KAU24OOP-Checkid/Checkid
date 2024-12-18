package com.example.checkid.view

import android.app.Application
import com.example.checkid.model.DataStoreManager
import com.google.firebase.FirebaseApp

class Checkid : Application() {
    override fun onCreate() {
        super.onCreate()

        FirebaseApp.initializeApp(this)
    }
}
package com.example.checkid.view

import android.app.Application
import com.google.firebase.FirebaseApp

class Checkid : Application() {
    override fun onCreate() {
        super.onCreate()

        FirebaseApp.initializeApp(this)
    }
}
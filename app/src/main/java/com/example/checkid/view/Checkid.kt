package com.example.checkid.view

import android.app.Application
import com.example.checkid.model.DataStoreManager
import com.google.firebase.FirebaseApp
import kotlinx.coroutines.runBlocking

class Checkid : Application() {
    override fun onCreate() {
        super.onCreate()

        FirebaseApp.initializeApp(this)

        val a = runBlocking {
            DataStoreManager.setUserId(applicationContext, "parent_test")
            DataStoreManager.setUserType(applicationContext, "Parent_test")
            DataStoreManager.setUserPartnerId(applicationContext, "child_test")
        }


    }
}
package com.example.checkid.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.checkid.view.activity.BaseActivity

class MyReceiver(private val activityCallback: (String) -> Unit) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent?.action
        Log.d("MyReceiver", "Received action: $action")
        if (action == "LOGIN_SUCCESS" || action == "PERMISSION_SUCCESS") {
            activityCallback(action) // Callback으로 BaseActivity에 알림
        }
    }
}

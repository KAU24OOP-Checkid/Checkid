package com.example.checkid.viewmodel

import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.lifecycle.ViewModel
import com.example.checkid.model.PermissionManager

class PermissionViewModel: ViewModel() {
    fun getAllPermissions() :Array<String> = PermissionManager.getAllPermissions()

    fun checkAllPermissions(context: Context): Boolean {
        return PermissionManager.arePermissionsGranted(context)
    }

    fun openAppSettings(context: Context) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = android.net.Uri.fromParts("package", context.packageName, null)
        }

        context.startActivity(intent)
    }
}
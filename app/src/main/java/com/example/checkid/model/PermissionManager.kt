package com.example.checkid.model

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

object PermissionManager {
    private val permissions = arrayOf(
        android.Manifest.permission.INTERNET,
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION,
        android.Manifest.permission.PACKAGE_USAGE_STATS,
        android.Manifest.permission.POST_NOTIFICATIONS
    )

    fun getAllPermissions(): Array<String> = permissions

    fun arePermissionsGranted(context: Context): Boolean {
        return permissions.all { permission ->
            ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
        }
    }
}
package com.example.checkid.viewmodel

import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.lifecycle.ViewModel
import com.example.checkid.model.PermissionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PermissionViewModel: ViewModel() {
    private val _permissionResult = MutableStateFlow(false)
    val permissionResult: StateFlow<Boolean> = _permissionResult

    fun getAllPermissions() :Array<String> = PermissionManager.getAllPermissions()

    fun checkAllPermissions(context: Context): Boolean {
        val allGranted = PermissionManager.arePermissionsGranted(context)
        _permissionResult.value = allGranted

        return allGranted

    }

    fun updatePermissionGranted() {
        _permissionResult.value = true
    }

    fun openAppSettings(context: Context) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = android.net.Uri.fromParts("package", context.packageName, null)
        }

        context.startActivity(intent)
    }
}
package com.example.checkid.viewmodel

import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.checkid.model.PermissionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PermissionViewModel: ViewModel() {
    private val _permissionResult = MutableLiveData<Boolean?>(null)
    val permissionResult: LiveData<Boolean?> = _permissionResult

    fun getAllPermissions() :Array<String> = PermissionManager.getAllPermissions()

    fun checkAllPermissions(context: Context): Boolean {
        return PermissionManager.arePermissionsGranted(context).also {
            _permissionResult.postValue(it)
        }
    }

    fun openAppSettings(context: Context) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = android.net.Uri.fromParts("package", context.packageName, null)
        }

        context.startActivity(intent)
    }
}
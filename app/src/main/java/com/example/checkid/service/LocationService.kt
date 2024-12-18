package com.example.checkid.service


import android.app.Service
import android.content.Intent
import android.location.Location
import android.os.IBinder
import android.util.Log
import com.example.checkid.model.Location as ChildLocation
import com.example.checkid.utils.FirebaseHelper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.Priority
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.os.Looper

class LocationService : Service() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val TAG = "LocationService"
    private val childId = "child1" // 실제 자녀 ID로 변경 필요

    override fun onCreate() {
        super.onCreate()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        startLocationUpdates()
    }

    private fun startLocationUpdates() {
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000L)
            .setMinUpdateIntervalMillis(5000L)
            .build()

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    Log.d(TAG, "Location: ${location.latitude}, ${location.longitude}")
                    // Firebase에 위치 전송
                    CoroutineScope(Dispatchers.IO).launch {
                        FirebaseHelper.updateChildLocation(childId, ChildLocation(location.latitude, location.longitude))
                    }
                }
            }
        }


        //런타임 위치 권한을 체크해야함
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())

    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}

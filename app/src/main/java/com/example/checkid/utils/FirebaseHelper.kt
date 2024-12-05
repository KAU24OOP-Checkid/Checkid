package com.example.checkid.utils

import android.util.Log
import com.example.checkid.model.Location
import com.google.firebase.database.*
import kotlinx.coroutines.tasks.await


object FirebaseHelper {

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private const val TAG = "FirebaseHelper"

    /**
     * 자녀의 위치를 업데이트하는 함수
     * @param childId 자녀의 고유 ID
     * @param location Location 객체
     */
    suspend fun updateChildLocation(childId: String, location: Location) {
        try {
            val locationRef = database.getReference("children_locations/$childId")
            locationRef.setValue(location).await()
            Log.d(TAG, "Location updated for $childId: $location")
        } catch (e: Exception) {
            Log.e(TAG, "Error updating location for $childId", e)
        }
    }

    /**
     * 자녀의 위치를 가져오는 suspend 함수
     * @param childId 자녀의 고유 ID (예: "child1")
     * @return Location 객체 또는 null
     */
    suspend fun getChildLocation(childId: String): Location? {
        return try {
            val locationRef = database.getReference("children_locations/$childId")
            val dataSnapshot = locationRef.get().await()
            dataSnapshot.getValue(Location::class.java)
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching child location", e)
            null
        }
    }

    /**
     * 실시간으로 자녀의 위치를 감지하고 콜백을 호출하는 함수
     * @param childId 자녀의 고유 ID
     * @param callback 위치 업데이트 시 호출되는 람다
     */
    fun listenToChildLocation(childId: String, callback: (Location?) -> Unit) {
        val locationRef = database.getReference("children_locations/$childId")
        locationRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val location = snapshot.getValue(Location::class.java)
                callback(location)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(TAG, "Failed to read child location", error.toException())
                callback(null)
            }
        })
    }
}

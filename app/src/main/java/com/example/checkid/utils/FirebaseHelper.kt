package com.example.checkid.utils

import android.util.Log
import com.example.checkid.model.Location
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

object FirebaseHelper {

    private val db = Firebase.firestore

    /**
     * 자녀 위치 데이터를 Firestore에 업데이트합니다.
     */
    suspend fun updateChildLocation(childId: String, location: Location) {
        try {
            val locationData = mapOf(
                "latitude" to location.latitude,
                "longitude" to location.longitude
            )

            db.collection("Location").document(childId)
                .set(locationData)
                .await()

            Log.d("FirebaseHelper", "Location updated for childId: $childId")
        } catch (e: Exception) {
            Log.e("FirebaseHelper", "Failed to update location for childId: $childId", e)
        }
    }
}

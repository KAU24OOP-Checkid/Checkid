package com.example.checkid.model

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

object LocationRepository {
    private const val COLLECTION = "Location"

    private const val DOCUMENT_LATITUDE = "latitude"
    private const val DOCUMENT_LONGITUDE = "longitude"

    private const val DEFAULT_LATITUDE = 0.0
    private const val DEFAULT_LONGITUDE = 0.0

    /**
     * Firestore에서 특정 ID에 해당하는 위치 데이터를 가져옵니다.
     */
    suspend fun getLocationById(id: String): Location? {
        Log.d("LocationRepository", "Attempting to fetch location for ID: $id")

        val db = Firebase.firestore

        return try {
            if (id.isEmpty()) {
                Log.e("LocationRepository", "Failed to fetch location: ID cannot be empty.")
                return null
            }

            val documentSnapshot = db.collection(COLLECTION).document(id).get().await()

            if (documentSnapshot.exists()) {
                val latitude = documentSnapshot.getDouble(DOCUMENT_LATITUDE) ?: DEFAULT_LATITUDE
                val longitude = documentSnapshot.getDouble(DOCUMENT_LONGITUDE) ?: DEFAULT_LONGITUDE
                Log.d(
                    "LocationRepository",
                    "Successfully fetched location for ID: $id, Latitude: $latitude, Longitude: $longitude"
                )
                Location(latitude, longitude)
            } else {
                Log.w("LocationRepository", "No document found for ID: $id in collection $COLLECTION.")
                null
            }
        } catch (e: Exception) {
            Log.e("LocationRepository", "Error occurred while fetching location for ID: $id", e)
            null
        }
    }

    /**
     * Firestore에 특정 ID로 위치 데이터를 저장합니다.
     */
    suspend fun saveLocationById(id: String, latitude: Double, longitude: Double) {
        val db = Firebase.firestore
        Log.d("LocationRepository", "Attempting to save location for ID: $id")

        try {
            if (id.isEmpty()) {
                Log.e("LocationRepository", "Failed to save location: ID cannot be empty.")
                return
            }

            val location = hashMapOf(
                DOCUMENT_LATITUDE to latitude,
                DOCUMENT_LONGITUDE to longitude
            )

            db.collection(COLLECTION).document(id).set(location).await()
            Log.d(
                "LocationRepository",
                "Successfully saved location for ID: $id, Latitude: $latitude, Longitude: $longitude"
            )
        } catch (e: Exception) {
            Log.e("LocationRepository", "Error occurred while saving location for ID: $id", e)
        }
    }
}

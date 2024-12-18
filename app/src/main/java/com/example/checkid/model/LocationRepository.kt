package com.example.checkid.model

import android.content.Context
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

object LocationRepository {
    private const val COLLECTION = "Location"

    private const val DOCUMENT_LATITUDE = "latitude"
    private const val DOCUMENT_LONGITUDE = "longitude"

    private const val DEFAULT_LATITUDE = 0.0
    private const val DEFAULT_LONGITUDE = 0.0

    suspend fun getLocationById(id: String) : Location?{
        val db = Firebase.firestore
        val documentRef = db.collection(COLLECTION).document(id)

        return try {
            val documentSnapShot = db.collection(COLLECTION).document(id).get().await()

            if (documentSnapShot.exists()) {
                Location(
                    documentSnapShot.getDouble(DOCUMENT_LATITUDE) ?: DEFAULT_LATITUDE,
                    documentSnapShot.getDouble(DOCUMENT_LONGITUDE) ?: DEFAULT_LONGITUDE
                )
            }

            else {
                null
            }

        } catch (e: Exception) {
            null
        }
    }

    suspend fun saveLocationById(id: String, latitude: Double, longitude: Double) {
        val db = Firebase.firestore

        try {
            val location = hashMapOf(
                DOCUMENT_LATITUDE to latitude,
                DOCUMENT_LONGITUDE to longitude
            )

            db.collection(COLLECTION).document(id).set(location).await()

        } catch (e: Exception) {
            //
        }
    }
}
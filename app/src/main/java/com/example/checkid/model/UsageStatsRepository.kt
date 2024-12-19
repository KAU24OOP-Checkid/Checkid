package com.example.checkid.model

import android.app.usage.UsageStats
import android.content.Context
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

object UsageStatsRepository {
    var usageStats = ArrayList<UsageStats>()

    private const val COLLECTION = "UsageStatsRepository"

    private const val DOCUMENT_PACKAGE_NAME = "packageName"
    private const val DOCUMENT_TOTAL_TIME_IN_FOREGROUND = "totalTimeInForeground"
    private const val DOCUMENT_LAST_TIME_USED = "lastTimeUsed"

    suspend fun uploadUsageStats(context: Context, usageStatsList: List<UsageStats>) {
        val id = DataStoreManager.getUserId(context)
        val db = Firebase.firestore

        try {
            db.collection(COLLECTION)
                .document(id)
                .set(usageStatsList)
                .await()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun fetchUsageStats(context: Context) : List<UsageStatsData>? {
        val id = DataStoreManager.getUserId(context)
        val db = Firebase.firestore

        try {


            return emptyList()
        } catch (e: Exception) {
            e.printStackTrace()

            return null
        }
    }
}

package com.example.checkid.manager

import com.example.checkid.model.UsageStatsData
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.FirebaseFirestore

object FirebaseManagerStatstics {

    fun uploadUsageStats(userId: String, usageStatsList: List<UsageStatsData>) {
        val db = FirebaseFirestore.getInstance()

        if (usageStatsList.isNotEmpty()) {
            // 부모/자녀의 Firestore 컬렉션에 업로드
            val userDocRef: DocumentReference = db.collection("ParentUser").document(userId)

            // UsageStatsList를 'usageStats' 필드에 추가
            userDocRef.set(
                hashMapOf("usageStats" to usageStatsList),
                SetOptions.merge()
            )
                .addOnSuccessListener {
                    println("Usage stats uploaded successfully")
                }
                .addOnFailureListener { e ->
                    println("Error uploading stats: $e")
                }
        }
    }

    // 사용 통계 데이터를 Firestore에서 불러오기
    fun fetchUsageStats(userId: String, callback: (List<UsageStatsData>) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val userDocRef: DocumentReference = db.collection("ParentUser").document(userId)

        userDocRef.get()
            .addOnSuccessListener { documentSnapshot ->
                val usageStatsList = mutableListOf<UsageStatsData>()
                val usageStats = documentSnapshot.get("usageStats") as? List<Map<String, Any>>?
                usageStats?.forEach { stat ->
                    val usageStatData = UsageStatsData(
                        packageName = stat["packageName"] as? String ?: "",
                        totalTimeInForeground = (stat["totalTimeInForeground"] as? Long) ?: 0L,
                        lastTimeUsed = (stat["lastTimeUsed"] as? Long) ?: 0L
                    )
                    usageStatsList.add(usageStatData)
                }
                callback(usageStatsList)
            }
            .addOnFailureListener { exception ->
                println("Error getting documents: $exception")
            }
    }
}

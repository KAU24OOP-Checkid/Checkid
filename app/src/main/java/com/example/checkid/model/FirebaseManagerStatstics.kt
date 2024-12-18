package com.example.checkid.manager

import com.example.checkid.model.UsageStatsData
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore

object FirebaseManagerStatstics {



    // UsageStats 데이터를 ParentUser 또는 ChildUser 문서에 업로드
    fun uploadUsageStats(userId: String, usageStatsList: List<UsageStatsData>) {
        val db = Firebase.firestore

        if (usageStatsList.isNotEmpty()) {
            // 부모/자녀의 Firestore 컬렉션에 업로드
            val userDocRef: DocumentReference = db.collection("users").document(userId)

            // UsageStatsList를 'usageStats' 필드에 추가
            userDocRef.set(
                hashMapOf("usageStats" to usageStatsList),
                SetOptions.merge()  // 기존 데이터를 덮어쓰지 않도록 merge 사용
            )
        }
    }




    // 사용 통계 데이터를 Firestore에서 불러오기
    fun fetchUsageStats(userId: String, callback: (List<UsageStatsData>) -> Unit) {
        // 사용자 Firestore 문서에서 usageStats 가져오기
        val db = Firebase.firestore

        val userDocRef: DocumentReference = db.collection("users").document(userId)

        userDocRef.get()
            .addOnSuccessListener { documentSnapshot ->
                val usageStatsList = mutableListOf<UsageStatsData>()
                // Firestore에서 'usageStats' 필드를 가져와서 List로 변환
                val usageStats = documentSnapshot.get("usageStats") as? List<Map<String, Any>>?
                usageStats?.forEach { stat ->
                    // UsageStatsData로 변환하여 리스트에 추가
                    val usageStatData = UsageStatsData(
                        packageName = stat["packageName"] as? String ?: "",
                        totalTimeInForeground = (stat["totalTimeInForeground"] as? Long) ?: 0L,
                        lastTimeUsed = (stat["lastTimeUsed"] as? Long) ?: 0L
                    )
                    usageStatsList.add(usageStatData)
                }
                callback(usageStatsList)
            }

    }
}

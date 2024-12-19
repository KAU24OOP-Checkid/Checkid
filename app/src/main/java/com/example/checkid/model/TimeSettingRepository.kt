// src/main/java/com/example/checkid/model/TimeSettingRepository.kt

package com.example.checkid.model

import com.google.api.Context
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

object TimeSettingRepository {

    private const val COLLECTION = "TimeSettings"
    private const val FIELD_TIME = "selected_time"

    /**
     * 시간을 Firestore에 저장합니다.
     * @param time "HH:mm" 형식의 시간 문자열
     * @return 저장 성공 여부
     */
    suspend fun saveTimeSetting(id: String, time: String): Boolean {
        val db = FirebaseFirestore.getInstance()

        val data = hashMapOf(
            FIELD_TIME to time
        )

        return try {
            db.collection(COLLECTION)
                .document(id)
                .set(data)
                .await()

            true

        } catch (e: Exception) {
            e.printStackTrace()

            false
        }
    }

    /**
     * Firestore에서 설정된 시간을 가져옵니다.
     * @return "HH:mm" 형식의 시간 문자열 또는 null
     */
    suspend fun getTimeSetting(id: String): String? {
        val db = FirebaseFirestore.getInstance()

        return try {
            val documentSnapshot = db.collection(COLLECTION).document(id).get().await()

            if (documentSnapshot.exists()) {
                documentSnapshot.getString(FIELD_TIME)

            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Firestore 실시간 리스너를 통해 시간 설정을 실시간으로 받아옵니다.
     * @param onTimeUpdate 시간 업데이트 시 호출되는 콜백
     */
    suspend fun listenTimeSetting(onTimeUpdate: (String?) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        //val id = DataStoreManager
        db.collection(COLLECTION).document("shared_time")
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    exception.printStackTrace()
                    onTimeUpdate(null)
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    onTimeUpdate(snapshot.getString(FIELD_TIME))
                } else {
                    onTimeUpdate(null)
                }
            }
    }
    /**
     * 저장된 시간 문자열을 파싱하여 시간과 분을 반환합니다.
     * @param time "HH:mm" 형식의 시간 문자열
     * @return Pair<Int, Int>? (시간, 분) 또는 null
     */
    fun parseTimeSetting(time: String?): Pair<Int, Int>? {
        return try {
            time?.split(":")?.let {
                val hour = it[0].toInt()
                val minute = it[1].toInt()
                Pair(hour, minute)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

}

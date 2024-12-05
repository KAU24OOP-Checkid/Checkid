package com.example.checkid.manager

import android.content.Context
import android.widget.Toast
import com.example.checkid.model.UsageStatsData
import com.google.firebase.database.*

class FirebaseManager(private val context: Context) {
    private val database: DatabaseReference = FirebaseDatabase.getInstance().getReference("UsageStats")

    fun uploadUsageStats(usageStatsList: List<UsageStatsData>) {
        if (usageStatsList.isNotEmpty()) {
            val userId = "A_user"
            database.child(userId).setValue(usageStatsList)
                .addOnSuccessListener {
                    Toast.makeText(context, "데이터가 성공적으로 업로드되었습니다.", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(context, "데이터 업로드에 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
        }
    }

    fun fetchUsageStats(callback: (List<UsageStatsData>) -> Unit) {
        val userId = "A_user"
        database.child(userId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val usageStatsList = mutableListOf<UsageStatsData>()
                for (dataSnapshot in snapshot.children) {
                    val usageStatsData = dataSnapshot.getValue(UsageStatsData::class.java)
                    if (usageStatsData != null) {
                        usageStatsList.add(usageStatsData)
                    }
                }
                callback(usageStatsList)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "데이터를 가져오는 데 실패했습니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

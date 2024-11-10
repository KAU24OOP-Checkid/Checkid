package com.example.checkid.model
/*
목적: Firestore에서 앱 사용 데이터를 가져와 RecyclerView에 표시하는 역할을 합니다.
기능:
Firestore에서 앱 사용 데이터 읽기: Firestore에서 appUsage 컬렉션의 데이터를 가져와 사용 통계를 표시합니다.
RecyclerView 어댑터 설정: 가져온 데이터를 Report 어댑터에 전달하고 RecyclerView에 표시
 */
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.checkid.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: Report // RecyclerView 어댑터

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)//
        recyclerView.layoutManager = LinearLayoutManager(this)

        fetchAppUsageData()
    }

    private fun fetchAppUsageData() {
        val db = FirebaseFirestore.getInstance() // Firestore 인스턴스를 가져옴
        db.collection("appUsage") // "appUsage" 컬렉션에 접근
            .get() // 데이터 가져오기
            .addOnSuccessListener { result:QuerySnapshot  -> // 성공적으로 데이터를 가져왔을 때 실행
                val usageList = mutableListOf<AppUsage>() // AppUsage 객체를 저장할 리스트 생성
                for (document in result) { // 결과에서 각 문서에 대해 반복
                    val packageName = document.getString("packageName") ?: "" // "packageName" 필드에서 문자열을 가져오고 없으면 빈 문자열
                    // 여기에서 Long으로 변환
                    val usageTime = document.getLong("usageTime")?.toLong() ?: 0L // "usageTime" 필드에서 Long 값을 가져오고 없으면 0으로 초기화
                    usageList.add(AppUsage(packageName, usageTime)) // AppUsage 객체를 리스트에 추가
                }
                adapter = Report(usageList) // UsageAdapter에 사용 통계 리스트를 전달하여 어댑터 생성
                recyclerView.adapter = adapter // RecyclerView에 어댑터 설정
            }
            .addOnFailureListener { exception -> // 데이터 가져오기 실패 시 실행
                // 데이터 가져오기 실패 처리
            }
    }




}

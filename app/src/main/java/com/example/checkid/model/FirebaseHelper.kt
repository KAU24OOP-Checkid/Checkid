package com.example.checkid.model
/*
역할: Firebase와의 통신을 전담하는 도우미 클래스이다.
기능:
Firebase Database에 데이터를 전송하는 메소드를 포함하고 있다.
sendUsageTimeToFirebase(appName: String, usageTime: Long) 메소드는 자녀의 앱 사용 시간을 Firebase에 저장하는 기능을 수행한다.
이 파일은 Firebase와의 연결 및 데이터 전송을 쉽게 관리할 수 있도록 돕는다.


private fun sendUsageTimeToFirebase(appName: String, usageTime: Long) {
    val database = FirebaseDatabase.getInstance().getReference("users/childId/appUsage")
    database.child(appName).setValue(mapOf("usageTime" to usageTime))
}

 */

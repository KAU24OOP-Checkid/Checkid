package com.example.checkid.model

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Report {
    // MutableList로 변경
    var applicationNames: MutableList<String> = mutableListOf()

    // java.time.LocalDateTime으로 변경
    var applicationTime: LocalDateTime = LocalDateTime.now()

    // 앱 이름 추가 메서드
    fun addApplicationName(name: String) {
        applicationNames.add(name)
    }

    // 앱 사용 시간을 기록하는 메서드
    fun setApplicationTime(time: LocalDateTime) {
        applicationTime = time
    }

    // 현재 앱 사용 시간 포맷팅 메서드
    fun getFormattedApplicationTime(): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        return applicationTime.format(formatter)
    }

    // Firebase에 저장하기 위한 메서드 (예시)
    fun toMap(): Map<String, Any> {
        return mapOf(
            "applicationNames" to applicationNames,
            "applicationTime" to applicationTime.toString()
        )
    }
}

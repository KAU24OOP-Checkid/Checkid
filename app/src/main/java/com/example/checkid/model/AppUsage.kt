package com.example.checkid.model

import android.os.Parcel
import android.os.Parcelable

//data class AppUsage(
//    val packageName: String, // 앱의 패키지 이름
//    val usageTime: Long // 앱 사용 시간 (밀리초 단위)
//)
data class AppUsage(
    val appName: String,
    val usageTime: Long
)
package com.example.checkid.model

data class UsageStatsData(
    val packageName: String = "",
    val totalTimeInForeground: Long = 0,
    val lastTimeUsed: Long = 0
)

package com.example.checkid.model

import android.app.usage.UsageStats

data class UsageStatsData(
    val packageName: String = "",
    val totalTimeInForeground: Long = 0,
    val lastTimeUsed: Long = 0

)


/*
fun UsageStats.toUsageStatsData(): UsageStatsData {
    return UsageStatsData(
        packageName = this.packageName,
        totalTimeInForeground = this.totalTimeInForeground,
        lastTimeUsed = this.lastTimeUsed
    )
}

 */
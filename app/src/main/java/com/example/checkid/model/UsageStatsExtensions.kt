
package com.example.checkid

import android.app.usage.UsageStats
import com.example.checkid.model.UsageStatsData

/// Extension function to convert UsageStats to UsageStatsData
fun UsageStats.toUsageStatsData(): UsageStatsData {
    return UsageStatsData(
        packageName = this.packageName,
        totalTimeInForeground = this.totalTimeInForeground,
        lastTimeUsed = this.lastTimeUsed
    )
}
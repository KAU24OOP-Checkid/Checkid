
package com.example.checkid.processor

import android.content.Context
import android.content.pm.PackageManager
import com.example.checkid.model.UsageStatsData
import java.util.*

class UsageStatisticsProcessor {
    data class UsageStatsText(
        val mostUsedAppText: String,
        val leastUsedAppText: String,
        val timesOverLimitText: String,
        val weeklyUsageText: String
    )

    fun processUsageStatistics(context: Context, usageStatsList: List<UsageStatsData>): UsageStatsText {
        val mostUsedApp = usageStatsList.maxByOrNull { it.totalTimeInForeground }
        val leastUsedApp = usageStatsList.minByOrNull { it.totalTimeInForeground }
        val usageLimit = 60 * 60 * 1000
        val timesOverLimit = usageStatsList.count { it.totalTimeInForeground > usageLimit }

        val weeklyUsage = usageStatsList.groupBy {
            val calendar = Calendar.getInstance().apply { timeInMillis = it.lastTimeUsed }
            calendar.get(Calendar.WEEK_OF_YEAR)
        }.mapValues { entry ->
            entry.value.sumOf { it.totalTimeInForeground / 1000 }
        }

        val mostUsedAppText = mostUsedApp?.let {
            val appName = try {
                val packageManager = context.packageManager
                val applicationInfo = packageManager.getApplicationInfo(it.packageName, 0)
                packageManager.getApplicationLabel(applicationInfo).toString()
            } catch (e: PackageManager.NameNotFoundException) {
                it.packageName
            }
            val totalTime = it.totalTimeInForeground / 1000
            val hours = totalTime / 3600
            val minutes = (totalTime % 3600) / 60
            val seconds = totalTime % 60
            "가장 많이 사용된 애플리케이션: $appName, 사용 시간: ${hours}시간 ${minutes}분 ${seconds}초"
        } ?: ""

        val leastUsedAppText = leastUsedApp?.let {
            val appName = try {
                val packageManager = context.packageManager
                val applicationInfo = packageManager.getApplicationInfo(it.packageName, 0)
                packageManager.getApplicationLabel(applicationInfo).toString()
            } catch (e: PackageManager.NameNotFoundException) {
                it.packageName
            }
            val totalTime = it.totalTimeInForeground / 1000
            val hours = totalTime / 3600
            val minutes = (totalTime % 3600) / 60
            val seconds = totalTime % 60
            "가장 적게 사용된 애플리케이션: $appName, 사용 시간: ${hours}시간 ${minutes}분 ${seconds}초"
        } ?: ""

        val weeklyUsageText = weeklyUsage.entries.joinToString(separator = "\n") { (week, totalUsage) ->
            val hours = totalUsage / 3600
            val minutes = (totalUsage % 3600) / 60
            val seconds = totalUsage % 60
            "$week 주 : 전체 애플리케이션 사용 시간: ${hours}시간 ${minutes}분 ${seconds}초"
        }

        return UsageStatsText(
            mostUsedAppText = mostUsedAppText,
            leastUsedAppText = leastUsedAppText,
            timesOverLimitText = "기준 시간 초과 횟수: $timesOverLimit",
            weeklyUsageText = weeklyUsageText
        )
    }
}

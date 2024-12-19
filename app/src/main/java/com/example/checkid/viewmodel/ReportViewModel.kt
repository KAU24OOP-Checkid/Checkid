package com.example.checkid.viewmodel

import android.app.Application
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.checkid.model.UsageStatsData
import java.util.*

class ReportViewModel(application: Application) : AndroidViewModel(application) {
    private val _usageStatsList = MutableLiveData<List<UsageStatsData>?>()
    val usageStatsList: LiveData<List<UsageStatsData>?> get() = _usageStatsList

    // UsageStats를 로드하는 메서드
    fun loadUsageStats() {
        val usageStatsManager = getApplication<Application>().getSystemService(UsageStatsManager::class.java)
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -1)  // 어제의 데이터
        val startTime = calendar.timeInMillis
        val endTime = System.currentTimeMillis()

        val statsList = usageStatsManager.queryUsageStats(
            UsageStatsManager.INTERVAL_DAILY, startTime, endTime
        ).filter { it.totalTimeInForeground > 0 } // 사용 시간이 0보다 큰 앱들만 필터링

        // UsageStats를 UsageStatsData로 변환하여 전달
        val usageStatsDataList = statsList.map { usageStats ->
            UsageStatsData(
                packageName = usageStats.packageName,
                totalTimeInForeground = usageStats.totalTimeInForeground,
                lastTimeUsed = usageStats.lastTimeUsed
            )
        }

        _usageStatsList.postValue(usageStatsDataList)
    }
}

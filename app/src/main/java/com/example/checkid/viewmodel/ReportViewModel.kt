package com.example.checkid.viewmodel

import android.app.Application
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.util.*

class ReportViewModel(application: Application) : AndroidViewModel(application) {
    private val _usageStatsList = MutableLiveData<List<UsageStats>>()
    val usageStatsList: LiveData<List<UsageStats>> get() = _usageStatsList

    fun loadUsageStats() {
        val usageStatsManager = getApplication<Application>().getSystemService(UsageStatsManager::class.java)
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -1)
        val startTime = calendar.timeInMillis
        val endTime = System.currentTimeMillis()

        val statsList = usageStatsManager.queryUsageStats(
            UsageStatsManager.INTERVAL_DAILY, startTime, endTime
        ).filter { it.totalTimeInForeground > 0 }

        _usageStatsList.postValue(statsList)
    }
}

class StatisticsViewModel(application: Application) : AndroidViewModel(application) {
    private val _usageStatsList = MutableLiveData<List<UsageStats>>()
    val usageStatsList: LiveData<List<UsageStats>> get() = _usageStatsList

    fun loadWeeklyUsageStats() {
        val usageStatsManager = getApplication<Application>().getSystemService(UsageStatsManager::class.java)
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -7)
        val startTime = calendar.timeInMillis
        val endTime = System.currentTimeMillis()

        val statsList = usageStatsManager.queryUsageStats(
            UsageStatsManager.INTERVAL_DAILY, startTime, endTime
        ).filter { it.totalTimeInForeground > 0 }

        _usageStatsList.postValue(statsList)
    }
}

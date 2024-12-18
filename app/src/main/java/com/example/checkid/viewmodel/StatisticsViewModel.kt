package com.example.checkid.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.checkid.manager.FirebaseManagerStatstics
import com.example.checkid.model.UsageStatsData
import com.example.checkid.workers.AppUsageWorkManager

class StatisticsViewModel(
    private val firebaseManager: FirebaseManagerStatstics,
    private val appUsageWorkManager: AppUsageWorkManager
) : ViewModel() {

    private val _fetchedUsageStats = MutableLiveData<List<UsageStatsData>>()
    val fetchedUsageStats: LiveData<List<UsageStatsData>> get() = _fetchedUsageStats

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    // Firebase에서 UsageStats 데이터 가져오기
    fun fetchUsageStats(userId: String) {
        _isLoading.postValue(true)
        firebaseManager.fetchUsageStats(userId) { usageStatsList ->
            _isLoading.postValue(false)
            _fetchedUsageStats.postValue(usageStatsList)
        }
    }

    // WorkManager로 주기적인 작업 예약
    fun scheduleAppUsageWork() {
        appUsageWorkManager.scheduleAppUsageWork()
    }


}


class StatisticsViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StatisticsViewModel::class.java)) {
            val appUsageWorkManager = AppUsageWorkManager(context)
            val firebaseManager = FirebaseManagerStatstics // 싱글톤 사용
            return StatisticsViewModel(firebaseManager, appUsageWorkManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

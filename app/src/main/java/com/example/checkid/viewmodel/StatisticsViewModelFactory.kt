package com.example.checkid.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.checkid.manager.FirebaseManagerStatstics
import com.example.checkid.workers.AppUsageWorkManager
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

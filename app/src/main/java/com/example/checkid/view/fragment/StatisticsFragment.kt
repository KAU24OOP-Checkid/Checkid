package com.example.checkid.view.fragment

import android.content.pm.PackageManager
import android.app.AppOpsManager
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.checkid.R
import com.example.checkid.databinding.FragmentStatisticsBinding
import com.example.checkid.model.UsageStatsAdapter
import java.util.*

class StatisticsFragment : Fragment() {
    private var _binding: FragmentStatisticsBinding? = null
    private val binding get() = _binding!!
    private lateinit var usageStatsList: List<UsageStats>
    private lateinit var adapter: UsageStatsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStatisticsBinding.inflate(inflater, container, false)
        val view = binding.root

        if (!UsagePermissionHelper.hasUsageStatsPermission(requireContext())) {
            UsagePermissionHelper.requestUsageStatsPermission(requireContext())
        } else {
            loadUsageStats() // 데이터를 로드합니다.
            setupRecyclerView()
            showUsageStatistics() // 추가된 기능 호출
        }

        return view
    }

    private fun loadUsageStats() {
        val usageStatsManager = requireContext().getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -7) // 주간 데이터를 가져옵니다.
        val startTime = calendar.timeInMillis
        val endTime = System.currentTimeMillis()

        // 전체 사용 통계를 가져온 후 사용 시간이 0초 이상인 항목만 필터링
        usageStatsList = usageStatsManager.queryUsageStats(
            UsageStatsManager.INTERVAL_DAILY, startTime, endTime
        ).filter { it.totalTimeInForeground > 0 }
    }

    private fun setupRecyclerView() {
        adapter = UsageStatsAdapter(usageStatsList)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter
    }

    private fun showUsageStatistics() {
        if (usageStatsList.isNotEmpty()) {
            val mostUsedApp = usageStatsList.maxByOrNull { it.totalTimeInForeground }
            val leastUsedApp = usageStatsList.minByOrNull { it.totalTimeInForeground }
            val usageLimit = 60 * 60 * 1000 // 예: 1시간을 기준으로 설정
            val timesOverLimit = usageStatsList.count { it.totalTimeInForeground > usageLimit }

            val weeklyUsage = usageStatsList.groupBy {
                val calendar = Calendar.getInstance().apply { timeInMillis = it.lastTimeUsed }
                calendar.get(Calendar.WEEK_OF_YEAR)
            }.mapValues { entry ->
                entry.value.sumOf { it.totalTimeInForeground / 1000 }
            }

            mostUsedApp?.let {
                val appName = try {
                    val packageManager = requireContext().packageManager
                    val applicationInfo = packageManager.getApplicationInfo(it.packageName, 0)
                    packageManager.getApplicationLabel(applicationInfo).toString()
                } catch (e: PackageManager.NameNotFoundException) {
                    it.packageName
                }
                val totalTime = it.totalTimeInForeground / 1000
                val hours = totalTime / 3600
                val minutes = (totalTime % 3600) / 60
                val seconds = totalTime % 60
                binding.mostUsedAppText.text = "가장 많이 사용된 애플리케이션: $appName, 사용 시간: ${hours}시간 ${minutes}분 ${seconds}초"
            }

            leastUsedApp?.let {
                val appName = try {
                    val packageManager = requireContext().packageManager
                    val applicationInfo = packageManager.getApplicationInfo(it.packageName, 0)
                    packageManager.getApplicationLabel(applicationInfo).toString()
                } catch (e: PackageManager.NameNotFoundException) {
                    it.packageName
                }
                val totalTime = it.totalTimeInForeground / 1000
                val hours = totalTime / 3600
                val minutes = (totalTime % 3600) / 60
                val seconds = totalTime % 60
                binding.leastUsedAppText.text = "가장 적게 사용된 애플리케이션: $appName, 사용 시간: ${hours}시간 ${minutes}분 ${seconds}초"
            }

            binding.timesOverLimitText.text = "기준 시간 초과 횟수: $timesOverLimit"

            val weeklyUsageText = weeklyUsage.entries.joinToString(separator = "\n") { (week, totalUsage) ->
                val hours = totalUsage / 3600
                val minutes = (totalUsage % 3600) / 60
                val seconds = totalUsage % 60
                "$week 주 : 전체 애플리케이션 사용 시간: ${hours}시간 ${minutes}분 ${seconds}초"
            }
            binding.weeklyUsageText.text = weeklyUsageText
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

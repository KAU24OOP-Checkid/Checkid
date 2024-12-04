package com.example.checkid.view.fragment

import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.checkid.databinding.FragmentReportBinding
import com.example.checkid.model.UsageStatsAdapter
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import java.util.*
import com.example.checkid.R
class ReportFragment : Fragment() {
    private var _binding: FragmentReportBinding? = null
    private val binding get() = _binding!!
    private lateinit var usageStatsList: List<UsageStats>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReportBinding.inflate(inflater, container, false)
        val view = binding.root

        loadUsageStats() // 앱 사용 데이터 로드
        setupRecyclerView()
        setupSummaryChart() // 전체 사용 시간 그래프 설정

        return view
    }

    private fun loadUsageStats() {
        val usageStatsManager = requireActivity().getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -1)
        val startTime = calendar.timeInMillis
        val endTime = System.currentTimeMillis()

        // 하루 동안의 사용 통계 가져오기
        usageStatsList = usageStatsManager.queryUsageStats(
            UsageStatsManager.INTERVAL_DAILY, startTime, endTime
        ).filter { it.totalTimeInForeground > 0 }
    }

    private fun setupRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = UsageStatsAdapter(usageStatsList)
    }

    private fun setupSummaryChart() {
        // 전체 사용 시간을 비율로 나타내기 위한 그래프 설정
        val totalUsageEntries = mutableListOf<BarEntry>()
        var index = 0f

        // 모든 앱 사용 시간 합산 후 각 앱의 사용 비율
        val totalUsageTime = usageStatsList.sumOf { it.totalTimeInForeground }
        usageStatsList.forEach { usageStats ->
            val usagePercentage = (usageStats.totalTimeInForeground.toFloat() / totalUsageTime) * 100
            totalUsageEntries.add(BarEntry(index, usagePercentage))
            index++
        }

        val dataSet = BarDataSet(totalUsageEntries, "앱별 사용 비율 (%)")
        dataSet.colors = listOf(
            requireContext().getColor(R.color.blue),
            requireContext().getColor(R.color.green),
            requireContext().getColor(R.color.yellow)
        )

        val barData = BarData(dataSet)
        binding.usageSummaryBarChart.data = barData
        binding.usageSummaryBarChart.description.isEnabled = false
        binding.usageSummaryBarChart.xAxis.isEnabled = false
        binding.usageSummaryBarChart.axisLeft.axisMinimum = 0f
        binding.usageSummaryBarChart.axisRight.isEnabled = false
        binding.usageSummaryBarChart.invalidate() // 그래프 업데이트
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

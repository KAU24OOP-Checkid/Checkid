package com.example.checkid.view.fragment

import android.app.usage.UsageStats
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.checkid.R
import com.example.checkid.databinding.FragmentStatisticsBinding
import com.example.checkid.model.UsageStatsAdapter
import com.example.checkid.viewmodel.StatisticsViewModel
import com.example.checkid.helper.UsagePermissionHelper

class StatisticsFragment : Fragment() {
    private var _binding: FragmentStatisticsBinding? = null
    private val binding get() = _binding!!
    private val statisticsViewModel: StatisticsViewModel by viewModels()
    private lateinit var usageStatsAdapter: UsageStatsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStatisticsBinding.inflate(inflater, container, false)
        checkAndRequestPermission()
        setupRecyclerView()
        setupObservers()
        statisticsViewModel.loadWeeklyUsageStats()
        return binding.root
    }

    private fun checkAndRequestPermission() {
        if (!UsagePermissionHelper.hasUsageStatsPermission(requireContext())) {
            UsagePermissionHelper.requestUsageStatsPermission(requireContext())
        }
    }

    private fun setupRecyclerView() {
        usageStatsAdapter = UsageStatsAdapter(emptyList())
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = usageStatsAdapter
        }
    }

    private fun setupObservers() {
        statisticsViewModel.usageStatsList.observe(viewLifecycleOwner) { usageStatsList ->
            usageStatsAdapter.updateUsageStats(usageStatsList)
            displayStatistics(usageStatsList)
        }
    }

    private fun displayStatistics(usageStatsList: List<UsageStats>) {
        val mostUsedApp = usageStatsList.maxByOrNull { it.totalTimeInForeground }
        val leastUsedApp = usageStatsList.minByOrNull { it.totalTimeInForeground }
        val usageLimit = 60 * 60 * 1000
        val timesOverLimit = usageStatsList.count { it.totalTimeInForeground > usageLimit }

        mostUsedApp?.let {
            binding.mostUsedAppText.text = getString(
                R.string.most_used_app_text,
                it.packageName,
                formatTime(it.totalTimeInForeground)
            )
        }

        leastUsedApp?.let {
            binding.leastUsedAppText.text = getString(
                R.string.least_used_app_text,
                it.packageName,
                formatTime(it.totalTimeInForeground)
            )
        }

        binding.timesOverLimitText.text = getString(R.string.times_over_limit_text, timesOverLimit)
    }

    private fun formatTime(milliseconds: Long): String {
        val seconds = milliseconds / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        return "$hours 시간 ${minutes % 60}분 ${seconds % 60}초"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

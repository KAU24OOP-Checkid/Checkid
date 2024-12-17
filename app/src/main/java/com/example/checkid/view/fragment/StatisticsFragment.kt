package com.example.checkid.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.checkid.databinding.FragmentStatisticsBinding
import com.example.checkid.helper.UsagePermissionHelper
import com.example.checkid.model.UsageStatsAdapter
import com.example.checkid.viewmodel.StatisticsViewModel
import com.example.checkid.manager.FirebaseManager
import com.example.checkid.processor.UsageStatisticsProcessor
import com.example.checkid.model.UsageStatsData
import com.example.checkid.toUsageStatsData

class StatisticsFragment : Fragment() {
    private var _binding: FragmentStatisticsBinding? = null
    private val binding get() = _binding!!
    private val statisticsViewModel: StatisticsViewModel by viewModels()
    private lateinit var adapter: UsageStatsAdapter
    private lateinit var firebaseManager: FirebaseManager
    private lateinit var usageStatisticsProcessor: UsageStatisticsProcessor

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStatisticsBinding.inflate(inflater, container, false)
        val view = binding.root

        // FirebaseManager 및 UsageStatisticsProcessor 초기화
        firebaseManager = FirebaseManager(requireContext())
        usageStatisticsProcessor = UsageStatisticsProcessor()

        // 권한 확인 및 요청
        if (!UsagePermissionHelper.hasUsageStatsPermission(requireContext())) {
            UsagePermissionHelper.requestUsageStatsPermission(requireContext())
        } else {
            setupRecyclerView()
            setupObservers()
            statisticsViewModel.loadWeeklyUsageStats() // ViewModel에서 주간 데이터 로드
        }

        return view
    }

    private fun setupRecyclerView() {
        adapter = UsageStatsAdapter(emptyList())
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter
    }

    private fun setupObservers() {
        statisticsViewModel.usageStatsList.observe(viewLifecycleOwner, Observer { usageStatsList ->
            // UsageStatsData를 UsageStats로 변환하여 사용
            val usageStatsDataList = usageStatsList.map { it.toUsageStatsData() }
            showUsageStatistics(usageStatsDataList) // 변환된 데이터로 UI 업데이트
            firebaseManager.uploadUsageStats(usageStatsDataList) // Firebase 업로드
            adapter.updateUsageStats(usageStatsDataList) // 변환된 데이터로 어댑터 업데이트
        })

        firebaseManager.fetchUsageStats { usageStatsList ->
            // Firebase에서 받은 데이터는 이미 UsageStats이므로 변환하지 않고 바로 어댑터에 전달
            adapter.updateUsageStats(usageStatsList)
        }
    }

    private fun showUsageStatistics(usageStatsList: List<UsageStatsData>) {
        val usageStatsText = usageStatisticsProcessor.processUsageStatistics(requireContext(), usageStatsList)
        binding.mostUsedAppText.text = usageStatsText.mostUsedAppText
        binding.leastUsedAppText.text = usageStatsText.leastUsedAppText
        binding.timesOverLimitText.text = usageStatsText.timesOverLimitText
        binding.weeklyUsageText.text = usageStatsText.weeklyUsageText
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

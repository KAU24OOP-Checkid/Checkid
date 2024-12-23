
package com.example.checkid.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.checkid.databinding.FragmentStatisticsBinding
import com.example.checkid.viewmodel.StatisticsViewModel
import com.example.checkid.processor.UsageStatisticsProcessor
import com.example.checkid.viewmodel.StatisticsViewModelFactory

class StatisticsFragment : Fragment() {
    private var _binding: FragmentStatisticsBinding? = null
    private val binding get() = _binding!!

    private val statisticsViewModel: StatisticsViewModel by viewModels {
        StatisticsViewModelFactory(requireContext())
    }

    private lateinit var adapter: UsageStatsAdapter
    private val userId: String = "userId" // 사용자 ID
    private val usageStatisticsProcessor = UsageStatisticsProcessor()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStatisticsBinding.inflate(inflater, container, false)
        setupRecyclerView()
        setupObservers()

        // Firebase 데이터 가져오기
        statisticsViewModel.fetchUsageStats(userId)


        return binding.root
    }

    private fun setupRecyclerView() {
        adapter = UsageStatsAdapter(emptyList())
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter
    }

    private fun setupObservers() {
        statisticsViewModel.fetchedUsageStats.observe(viewLifecycleOwner) { usageStatsList ->
            adapter.updateUsageStats(usageStatsList)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}



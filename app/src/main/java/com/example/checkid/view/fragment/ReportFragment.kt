package com.example.checkid.view.fragment
import android.app.usage.UsageStats
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.checkid.databinding.FragmentReportBinding
import com.example.checkid.model.UsageStatsAdapter
import com.example.checkid.viewmodel.ReportViewModel
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.example.checkid.R

class ReportFragment : Fragment() {
    private var _binding: FragmentReportBinding? = null
    private val binding get() = _binding!!
    private val reportViewModel: ReportViewModel by viewModels()
    private lateinit var usageStatsAdapter: UsageStatsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReportBinding.inflate(inflater, container, false)
        setupRecyclerView()
        // setupObservers()
        reportViewModel.loadUsageStats()
        return binding.root
    }

    private fun setupRecyclerView() {
        usageStatsAdapter = UsageStatsAdapter(emptyList())
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = usageStatsAdapter
        }
    }


    private fun updateChart(usageStatsList: List<UsageStats>) {
        val totalUsage = usageStatsList.sumOf { it.totalTimeInForeground }
        val entries = usageStatsList.mapIndexed { index, stats ->
            val percentage = (stats.totalTimeInForeground.toFloat() / totalUsage) * 100
            BarEntry(index.toFloat(), percentage)
        }
        val dataSet = BarDataSet(entries, "앱별 사용 비율 (%)").apply {
            colors = listOf(
                requireContext().getColor(R.color.blue),
                requireContext().getColor(R.color.green),
                requireContext().getColor(R.color.yellow)
            )
        }
        binding.usageSummaryBarChart.apply {
            data = BarData(dataSet)
            description.isEnabled = false
            xAxis.isEnabled = false
            axisLeft.axisMinimum = 0f
            axisRight.isEnabled = false
            invalidate()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
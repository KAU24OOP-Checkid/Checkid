package com.example.checkid.model

import android.app.usage.UsageStats
import android.content.pm.PackageManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.checkid.R
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import java.text.SimpleDateFormat
import java.util.*

class UsageStatsAdapter(private val usageStatsList: List<UsageStats>) :
    RecyclerView.Adapter<UsageStatsAdapter.UsageStatsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsageStatsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_usage_stats, parent, false)
        return UsageStatsViewHolder(view)
    }

    override fun onBindViewHolder(holder: UsageStatsViewHolder, position: Int) {
        val usageStats = usageStatsList[position]
        val packageManager = holder.itemView.context.packageManager

        // 앱 이름 가져오기
        val appName = try {
            val applicationInfo = packageManager.getApplicationInfo(usageStats.packageName, 0)
            packageManager.getApplicationLabel(applicationInfo).toString()
        } catch (e: PackageManager.NameNotFoundException) {
            usageStats.packageName
        }

        // 마지막 사용 시간 및 총 사용 시간 계산
        val lastTimeUsed = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            .format(Date(usageStats.lastTimeUsed))
        val totalTimeInForeground = usageStats.totalTimeInForeground / 1000

        // UI 업데이트
        holder.appNameTextView.text = appName
        holder.lastTimeUsedTextView.text = "마지막 사용: $lastTimeUsed"
        holder.totalTimeTextView.text = "총 사용 시간: ${totalTimeInForeground / 3600}시간 ${(totalTimeInForeground % 3600) / 60}분 ${totalTimeInForeground % 60}초"

        // 앱 아이콘 설정
        try {
            val icon = packageManager.getApplicationIcon(usageStats.packageName)
            holder.appIconImageView.setImageDrawable(icon)
        } catch (e: PackageManager.NameNotFoundException) {
            holder.appIconImageView.setImageResource(R.drawable.ic_default_app_icon) // 기본 아이콘
        }

        // 전체 사용 시간 계산
        val totalUsageTime = usageStatsList.sumOf { it.totalTimeInForeground.toInt() } //toFloat?

        // 막대 그래프 설정 (전체 사용 시간 대비 비율 계산)
        val entries = usageStatsList.mapIndexed { index, stats ->
            BarEntry(index.toFloat(), stats.totalTimeInForeground.toFloat() / totalUsageTime * 100)
        }.toMutableList()
/*
        val dataSet = BarDataSet(entries, "앱 사용 시간 비율 (%)")
        dataSet.color = holder.itemView.context.getColor(R.color.black)
        val barData = BarData(dataSet)

        holder.usageBarChart.data = barData
        holder.usageBarChart.description.isEnabled = false
        holder.usageBarChart.axisLeft.axisMinimum = 0f
        holder.usageBarChart.axisRight.isEnabled = false
        holder.usageBarChart.xAxis.isEnabled = false
        holder.usageBarChart.invalidate() // 그래프 업데이트

 */
    }


    override fun getItemCount(): Int = usageStatsList.size

    inner class UsageStatsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val appIconImageView: ImageView = itemView.findViewById(R.id.appIconImageView)
        val appNameTextView: TextView = itemView.findViewById(R.id.appNameTextView)
        val lastTimeUsedTextView: TextView = itemView.findViewById(R.id.lastTimeUsedTextView)
        val totalTimeTextView: TextView = itemView.findViewById(R.id.totalTimeTextView)
        //val usageBarChart: BarChart = itemView.findViewById(R.id.usageBarChart)
    }
}

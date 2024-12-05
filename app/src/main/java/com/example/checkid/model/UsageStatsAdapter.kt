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
import java.text.SimpleDateFormat
import java.util.*

class UsageStatsAdapter(private var usageStatsList: List<UsageStatsData>) :
    RecyclerView.Adapter<UsageStatsAdapter.UsageStatsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsageStatsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_usage_stats, parent, false)
        return UsageStatsViewHolder(view)
    }

    override fun onBindViewHolder(holder: UsageStatsViewHolder, position: Int) {
        val usageStatsData = usageStatsList[position]
        val packageManager = holder.itemView.context.packageManager

        // 앱 이름 가져오기
        val appName = try {
            val applicationInfo = packageManager.getApplicationInfo(usageStatsData.packageName, 0)
            packageManager.getApplicationLabel(applicationInfo).toString()
        } catch (e: PackageManager.NameNotFoundException) {
            usageStatsData.packageName
        }

        // 마지막 사용 시간 및 총 사용 시간 계산
        val lastTimeUsed = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            .format(Date(usageStatsData.lastTimeUsed))
        val totalTimeInForeground = usageStatsData.totalTimeInForeground / 1000

        // UI 업데이트
        holder.appNameTextView.text = appName
        holder.lastTimeUsedTextView.text = "마지막 사용: $lastTimeUsed"
        holder.totalTimeTextView.text = "총 사용 시간: ${totalTimeInForeground / 3600}시간 ${(totalTimeInForeground % 3600) / 60}분 ${totalTimeInForeground % 60}초"

        // 앱 아이콘 설정
        try {
            val icon = packageManager.getApplicationIcon(usageStatsData.packageName)
            holder.appIconImageView.setImageDrawable(icon)
        } catch (e: PackageManager.NameNotFoundException) {
            holder.appIconImageView.setImageResource(R.drawable.ic_default_app_icon) // 기본 아이콘
        }
    }

    override fun getItemCount(): Int = usageStatsList.size

    fun updateUsageStats(newStatsList: List<UsageStatsData>) {
        // 제외할 패키지 정의
        val excludedPackages = listOf(
            "com.google.android.apps.nexuslauncher", // 런처 앱
            "com.android.systemui",                 // 시스템 UI
            "com.google.android.inputmethod.latin", // 키보드 앱
            "com.example.usagestatsmanagerapitest"  // 테스트 앱
        )

        // 필터링 로직
        this.usageStatsList = newStatsList.filter { stats ->
            stats.packageName !in excludedPackages && stats.totalTimeInForeground > 0
        }
        notifyDataSetChanged()
    }

    inner class UsageStatsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val appIconImageView: ImageView = itemView.findViewById(R.id.appIconImageView)
        val appNameTextView: TextView = itemView.findViewById(R.id.appNameTextView)
        val lastTimeUsedTextView: TextView = itemView.findViewById(R.id.lastTimeUsedTextView)
        val totalTimeTextView: TextView = itemView.findViewById(R.id.totalTimeTextView)
    }
}


// Extension function to convert UsageStats to UsageStatsData
fun UsageStats.toUsageStatsData(): UsageStatsData {
    return UsageStatsData(
        packageName = this.packageName,
        totalTimeInForeground = this.totalTimeInForeground,
        lastTimeUsed = this.lastTimeUsed
    )
}

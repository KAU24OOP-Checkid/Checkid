package com.example.checkid.model
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.checkid.R
import com.example.checkid.model.AppUsage

// 자녀의 휴대폰에서 앱별 사용시간을 파이어베이스로 보냄


import android.app.usage.UsageStatsManager
import android.content.Context

/*
class Report {
    // Mutablelist로 바꾸기
    var applicationName: String = ""

    // java.time로 바꾸기
    var applicationTime: Int = 0

    // method
}

 */

class Report(private val usageList: List<AppUsage>) : RecyclerView.Adapter<Report.ReportViewHolder>() {

    // ViewHolder 클래스 정의
    class ReportViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val appNameTextView: TextView = itemView.findViewById(R.id.appName)  // 앱 이름을 표시할 TextView
        val usageTimeTextView: TextView = itemView.findViewById(R.id.usageTime)  // 사용 시간을 표시할 TextView
    }

    // onCreateViewHolder: 새로운 아이템 뷰를 생성하는 함수
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.report_item, parent, false)
        return ReportViewHolder(itemView)
    }

    // onBindViewHolder: 아이템 뷰에 데이터를 바인딩하는 함수
    override fun onBindViewHolder(holder: ReportViewHolder, position: Int) {
        val currentItem = usageList[position]
        holder.appNameTextView.text = currentItem.appName
        holder.usageTimeTextView.text = formatTime(currentItem.usageTime)
    }

    // getItemCount: RecyclerView에 표시할 아이템 개수
    override fun getItemCount(): Int {
        return usageList.size
    }

    // 사용 시간 포맷팅 (초를 시:분:초 형식으로 변환)
    private fun formatTime(seconds: Long): String {
        val hours = seconds / 3600
        val minutes = (seconds % 3600) / 60
        val remainingSeconds = seconds % 60
        return String.format("%02d:%02d:%02d", hours, minutes, remainingSeconds)
    }
}

/*


    // getItemCount: RecyclerView에 표시할 아이템 개수
    override fun getItemCount(): Int {
        return usageList.size
    }
}


*/

/*
private fun getAppUsageTime(context: Context) {  // Context를 파라미터로 받도록 변경
    val usageStatsManager = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
    val endTime = System.currentTimeMillis()
    val startTime = endTime - (1000 * 60 * 60 * 24) // 지난 24시간 동안
    val usageStatsList = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, startTime, endTime)

    usageStatsList.forEach { usageStats ->
        val appName = usageStats.packageName
        val usageTime = usageStats.totalTimeInForeground / 1000 // 초 단위로 변환
        sendUsageTimeToFirebase(appName, usageTime)
    }
}


private fun sendUsageTimeToFirebase(appName: String, usageTime: Long) {
    val database = FirebaseDatabase.getInstance().getReference("users/childId/appUsage")
    database.child(appName).setValue(mapOf("usageTime" to usageTime))
}
*/
package com.example.checkid.workers

import android.content.Context
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.app.NotificationManager
import android.os.Build
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.core.app.NotificationCompat
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.*
import java.util.concurrent.TimeUnit

class AppUsageWorker(
    context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    override fun doWork(): Result {
        // 앱 사용 시간 확인 및 알림 보내기
        checkAppUsageAndSendNotifications()

        // 작업이 성공적으로 완료되었음을 반환
        return Result.success()
    }

    private fun checkAppUsageAndSendNotifications() {
        val usageStatsManager = applicationContext.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -1)
        val startTime = calendar.timeInMillis
        val endTime = System.currentTimeMillis()

        val statsList = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, startTime, endTime)
            .filter { it.totalTimeInForeground > 0 }

        statsList.forEach { usageStats ->
            if (usageStats.totalTimeInForeground > 4 * 60 * 60 * 1000) { // 4시간 초과시 알림
                sendNotification(usageStats)
            }
        }
    }

    private fun sendNotification(usageStats: UsageStats) {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // 알림 채널 등록 (Android 8.0 이상에서 필요)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "AppUsageChannel"
            val channelName = "App Usage Notifications"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = android.app.NotificationChannel(channelId, channelName, importance)
            notificationManager.createNotificationChannel(channel)
        }

        // 앱 알림을 보내기 위한 Notification 객체 생성
        val notificationId = usageStats.packageName.hashCode() // 고유 ID 생성
        val notification = NotificationCompat.Builder(applicationContext, "AppUsageChannel")
            .setContentTitle("앱 사용 시간 초과")
            .setContentText("${usageStats.packageName} 앱을 너무 많이 사용했습니다.")
            //.setSmallIcon(R.drawable.ic_notification)
            .build()

        // 알림 발송
        notificationManager.notify(notificationId, notification)
    }
}
class AppUsageWorkManager(private val context: Context) {

    fun scheduleAppUsageWork() {
        val workRequest = PeriodicWorkRequestBuilder<AppUsageWorker>(24, TimeUnit.HOURS)
            .setInitialDelay(calculateInitialDelay(), TimeUnit.MILLISECONDS)
            .build()

        WorkManager.getInstance(context).enqueue(workRequest)
    }

    private fun calculateInitialDelay(): Long {
        val calendar = Calendar.getInstance()
        val currentTime = calendar.timeInMillis

        // 다음 5시까지의 남은 시간 계산
        calendar.set(Calendar.HOUR_OF_DAY, 5)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        val delay = calendar.timeInMillis - currentTime
        return if (delay > 0) delay else delay + 24 * 60 * 60 * 1000 // 다음 날 5시
    }
}


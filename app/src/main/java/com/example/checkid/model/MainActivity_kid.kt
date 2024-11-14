package com.example.checkid.model
/*
자녀의 앱 사용 통계를 추적할 수 있는 권한을 요청하고, 해당 권한이 부여되면 앱 사용 시간을 가져오는 역할
권한 확인 (isUsagePermissionGranted()): 앱 사용 시간 추적 권한이 있는지 체크.
권한 요청 (requestUsagePermission()): 사용자가 권한을 승인할 수 있도록 설정 화면으로 이동.
 */
import android.app.AppOpsManager
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.provider.Settings
import com.example.checkid.R

class MainActivity_kid : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (isUsagePermissionGranted()) {
            getAppUsageTime()
        } else {
            requestUsagePermission()
        }
    }

    private fun getAppUsageTime() {
        val usageStatsManager = getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        // 나머지 코드...
    }

    private fun isUsagePermissionGranted(): Boolean {
        val appOps = getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, android.os.Process.myUid(), packageName)
        return mode == AppOpsManager.MODE_ALLOWED
    }

    private fun requestUsagePermission() {
        // 사용자에게 권한 요청하는 Intent
        startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
    }
}

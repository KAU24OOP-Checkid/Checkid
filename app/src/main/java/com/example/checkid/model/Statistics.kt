package com.example.checkid.model
/*
앱 사용 통계를 처리하는 Fragment로, 사용자가 자신의 앱 사용 정보를 확인할 수 있도록 도와주는 역할을 합니다.
이 클래스는 주로 권한 체크와 사용 통계 가져오기 기능을 담당합니다.
*/
import android.app.AppOpsManager
import android.app.usage.UsageStatsManager
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import android.content.Intent
import android.provider.Settings

class Statistics : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (isUsagePermissionGranted()) {
            getAppUsageTime()
        } else {
            requestUsagePermission()
        }
    }

    private fun getAppUsageTime() {
        val usageStatsManager = requireActivity().getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        // 나머지 코드...
    }

    private fun isUsagePermissionGranted(): Boolean {
        val appOps = requireActivity().getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, android.os.Process.myUid(), requireActivity().packageName)
        return mode == AppOpsManager.MODE_ALLOWED
    }

    private fun requestUsagePermission() {
        // 사용자에게 권한 요청하는 Intent
        startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
    }
}

package com.example.checkid.model
/*
역할: UI 요소를 포함한 Fragment로, 특정 사용자 인터페이스 기능을 구현하는 데 사용된다.
기능:
Fragment가 생성될 때 onViewCreated 메소드에서 권한을 확인하고, 사용 시간이 필요할 경우 적절한 메소드를 호출한다.
Fragment를 사용하면 UI의 특정 부분만 갱신할 수 있어 효율적이다.
getAppUsageTime()와 isUsagePermissionGranted(), requestUsagePermission() 메소드를 통해 앱 사용 통계를 요청하고, 권한을 체크하며, 권한 요청을 위한 Intent를 실행한다.
*/
import android.app.AppOpsManager
import android.app.usage.UsageStatsManager
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import android.content.Intent
import android.provider.Settings

class sageStatsFragment : Fragment() {

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

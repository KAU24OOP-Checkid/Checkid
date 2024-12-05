package com.example.checkid.helper

import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.widget.Toast

object UsagePermissionHelper {
    /**
     * Checks if the app has usage stats permission.
     * @return true if permission is granted, false otherwise.
     */
    fun hasUsageStatsPermission(context: Context): Boolean {
        val appOpsManager = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            appOpsManager.unsafeCheckOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(),
                context.packageName
            )
        } else {
            appOpsManager.checkOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(),
                context.packageName
            )
        }
        return mode == AppOpsManager.MODE_ALLOWED
    }

    /**
     * Requests the usage stats permission.
     * Shows a toast message and redirects the user to the settings page.
     */
    fun requestUsageStatsPermission(context: Context) {
        Toast.makeText(
            context,
            "이 앱은 사용 통계를 보기 위해 권한이 필요합니다.\n[앱 사용 접근]에서 권한을 부여해주세요.",
            Toast.LENGTH_LONG
        ).show()
        val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
    }
}

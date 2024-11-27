package com.example.checkid.view.fragment

import android.app.AlertDialog
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import androidx.preference.EditTextPreference
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.example.checkid.R
import com.example.checkid.view.fragment.MapsFragment
import com.example.checkid.utils.NotificationUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class SettingsFragment : PreferenceFragmentCompat() {

    // SharedPreferences 이름과 키 정의
    private val USER_PREFS = "user_prefs"
    private val USER_IDS_KEY = "user_ids"
    private val NOTIFICATION_MESSAGE_KEY = "notification_message"

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.setting_preference, rootKey)

        // Preference 클릭 리스너 설정
        setupPreferenceClickListeners()
        // Preference 변경 리스너 설정
        setupPreferenceChangeListeners()
        // EditTextPreference 클릭 시 요약에 입력한 내용을 반영
        setupNotificationMessageSummary()
    }

    private fun setupPreferenceClickListeners() {
        // "user_id" Preference
        val idPreference = findPreference<Preference>("user_id")
        idPreference?.setOnPreferenceClickListener {
            showUserListDialog(requireContext())
            true
        }

        // "announcement" Preference
        val announcementPreference = findPreference<Preference>("announcement")
        announcementPreference?.setOnPreferenceClickListener {
            showAnnouncementDialog()
            true
        }

        // "help" Preference
        val helpPreference = findPreference<Preference>("help")
        helpPreference?.setOnPreferenceClickListener {
            showHelpDialog()
            true
        }

        // "version_add" Preference
        val versionAddPreference = findPreference<Preference>("version_add")
        versionAddPreference?.setOnPreferenceClickListener {
            showVersionChangesDialog()
            true
        }
    }

    private fun setupPreferenceChangeListeners() {
        // SwitchPreferenceCompat
        val notificationsPreference = findPreference<SwitchPreferenceCompat>("notifications")
        notificationsPreference?.setOnPreferenceChangeListener { _, newValue ->
            val isEnabled = newValue as Boolean
            if (isEnabled) {
                enableNotifications()
            } else {
                disableNotifications()
            }
            true
        }

        // ListPreference
        val ringtonePreference = findPreference<ListPreference>("ringtone")
        ringtonePreference?.setOnPreferenceChangeListener { _, newValue ->
            val selectedRingtone = newValue as String
            setRingtone(selectedRingtone)
            true
        }
    }

    private fun setupNotificationMessageSummary() {
        val notificationMessagePref = findPreference<EditTextPreference>(NOTIFICATION_MESSAGE_KEY)
        notificationMessagePref?.summaryProvider =
            Preference.SummaryProvider<EditTextPreference> { preference ->
                if (preference.text.isNullOrEmpty()) {
                    "초과 시 자녀에게 보낼 메시지를 입력하세요."
                } else {
                    preference.text
                }
            }
    }

    override fun onPreferenceTreeClick(preference: Preference): Boolean {
        when (preference.key) {
            "open_map" -> {
                // MapsFragment로 이동
                parentFragmentManager.beginTransaction()
                    .replace(R.id.activity_main_FragmentContainerView, MapsFragment())
                    .addToBackStack(null) // 뒤로 가기 시 이전 프래그먼트로 돌아가도록 설정
                    .commit()
                return true
            }
        }
        return super.onPreferenceTreeClick(preference)
    }

    // 사용자 ID 목록을 표시하는 다이얼로그
    private fun showUserListDialog(context: Context) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("사용자 정보")

        // 현재 사용자 ID 목록 로드
        val userIds = loadUserIds()
        val userList = userIds.joinToString("\n")
        builder.setMessage(if (userList.isEmpty()) "추가된 ID가 없습니다." else userList)

        // 추가 버튼
        builder.setPositiveButton("추가") { _, _ -> showAddUserDialog(context) }
        // 삭제 버튼
        builder.setNeutralButton("삭제") { _, _ -> showDeleteUserDialog(context) }
        // 닫기 버튼
        builder.setNegativeButton("닫기", null)

        builder.show()
    }

    // 새로운 사용자 ID를 추가하는 다이얼로그
    private fun showAddUserDialog(context: Context) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("새 ID 추가")

        // 입력 필드 생성
        val input = EditText(context)
        input.hint = "새로운 사용자 ID를 입력하세요"

        val layout = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50, 40, 50, 10)
            addView(input)
        }

        builder.setView(layout)

        builder.setPositiveButton("추가") { _, _ ->
            val newUserId = input.text.toString().trim()
            if (newUserId.isNotEmpty()) {
                if (android.util.Patterns.EMAIL_ADDRESS.matcher(newUserId).matches()) {
                    val userIds = loadUserIds()
                    if (userIds.contains(newUserId)) {
                        // 이미 존재하는 ID일 경우
                        AlertDialog.Builder(context)
                            .setTitle("중복된 ID")
                            .setMessage("이미 존재하는 ID입니다.")
                            .setPositiveButton("확인", null)
                            .show()
                    } else {
                        userIds.add(newUserId)
                        saveUserIds(userIds) // 사용자 ID 저장
                        showUserListDialog(context)
                    }
                } else {
                    // 이메일 형식이 올바르지 않을 경우
                    AlertDialog.Builder(context)
                        .setTitle("입력 오류")
                        .setMessage("올바른 이메일 형식이 아닙니다.")
                        .setPositiveButton("확인", null)
                        .show()
                }
            } else {
                // 입력이 비어있을 경우
                AlertDialog.Builder(context)
                    .setTitle("입력 오류")
                    .setMessage("ID를 입력해주세요.")
                    .setPositiveButton("확인", null)
                    .show()
            }
        }
        builder.setNegativeButton("취소", null)

        builder.show()
    }

    // 사용자 ID를 삭제하는 다이얼로그
    private fun showDeleteUserDialog(context: Context) {
        val userIds = loadUserIds()
        if (userIds.isEmpty()) {
            // 삭제할 ID가 없으면 알림 표시
            AlertDialog.Builder(context)
                .setTitle("삭제 불가")
                .setMessage("삭제할 ID가 없습니다.")
                .setPositiveButton("확인", null)
                .show()
            return
        }

        val builder = AlertDialog.Builder(context)
        builder.setTitle("ID 삭제")

        // 사용자 ID를 배열로 변환
        val items = userIds.toTypedArray()
        builder.setItems(items) { _, which ->
            // 선택된 ID 삭제
            val removedId = userIds.removeAt(which)
            saveUserIds(userIds) // 사용자 ID 저장
            AlertDialog.Builder(context)
                .setTitle("ID 삭제됨")
                .setMessage("삭제된 ID: $removedId")
                .setPositiveButton("확인") { _, _ ->
                    showUserListDialog(context)
                }
                .show()
        }

        builder.setNegativeButton("취소", null)
        builder.show()
    }

    // 공지 사항을 표시하는 다이얼로그
    private fun showAnnouncementDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("공지 사항")
            .setMessage("")
            .setPositiveButton("확인", null)
            .show()
    }

    // 도움말을 표시하는 다이얼로그
    private fun showHelpDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("도움말")
            .setMessage("")
            .setPositiveButton("확인", null)
            .show()
    }

    // 버전 변경사항을 표시하는 다이얼로그
    private fun showVersionChangesDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("버전 변경사항")
            .setMessage("1.0.0 ver 변경사항\n" +
                    "- 버그 픽스\n" +
                    "- 버그 픽스\n" +
                    "- 버그 픽스\n")
            .setPositiveButton("확인", null)
            .show()
    }

    // 알림을 활성화하는 함수
    private fun enableNotifications() {
        // 알림 채널 생성
        NotificationUtils.createNotificationChannel(requireContext())
        // 알림 전송 예제
        NotificationUtils.sendUsageAlertNotification(
            requireContext(),
            "알림 기능이 활성화되었습니다."
        )
    }

    // 알림을 비활성화하는 함수
    private fun disableNotifications() {
        // 알림 비활성화 로직 구현
    }

    // 선택한 알림음을 설정하는 함수
    private fun setRingtone(ringtone: String) {
        // 알림음 설정 로직 구현
    }

    // 사용자 ID를 SharedPreferences에 저장
    private fun saveUserIds(userIds: MutableList<String>) {
        val sharedPreferences = requireContext().getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putStringSet(USER_IDS_KEY, userIds.toSet())
            apply()
        }
    }

    // 사용자 ID를 SharedPreferences에서 로드
    private fun loadUserIds(): MutableList<String> {
        val sharedPreferences = requireContext().getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE)
        val savedUserIds = sharedPreferences.getStringSet(USER_IDS_KEY, null)
        return if (savedUserIds != null) {
            savedUserIds.toMutableList()
        } else {
            mutableListOf()
        }
    }

    // 특정 앱 사용 시간 초과 시 알림 전송
    private fun sendNotificationIfAppUsageExceeds(context: Context, packageName: String, maxUsageTime: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            val usageTime = getAppUsageTime(context, packageName)
            if (usageTime > maxUsageTime) {
                val sharedPreferences = requireContext().getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE)
                val message = sharedPreferences.getString(NOTIFICATION_MESSAGE_KEY, "앱 사용 시간이 초과되었습니다.")
                NotificationUtils.sendUsageAlertNotification(context, message ?: "앱 사용 시간이 초과되었습니다.")
            }
        }

    }
}

fun getAppUsageTime(context: Context, packageName: String): Long {
    val usageStatsManager =
        context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
    val endTime = System.currentTimeMillis()
    val startTime = endTime - (24 * 60 * 60 * 1000) // 지난 24시간 기준

    val stats: List<UsageStats> = usageStatsManager.queryUsageStats(
        UsageStatsManager.INTERVAL_DAILY,
        startTime,
        endTime
    )

    for (usageStat in stats) {
        if (usageStat.packageName == packageName) {
            return usageStat.totalTimeInForeground // 사용 시간이 밀리초로 반환됨
        }
    }
    return 0L
}
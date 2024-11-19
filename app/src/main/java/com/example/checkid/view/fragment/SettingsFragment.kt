package com.example.checkid.view

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.checkid.MapsActivity
import com.example.checkid.R
import android.content.Context
import android.widget.EditText
import android.widget.LinearLayout



class SettingsFragment : PreferenceFragmentCompat() {

    // 사용자 ID를 저장하는 리스트
    private val userIds = mutableListOf("abcd@abc.com", "adsf@asdf.com") // 초기 예제 데이터

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.setting_preference, rootKey)

        // "user_id" Preference를 찾기
        val idPreference = findPreference<Preference>("user_id")

        // 클릭 리스너 설정
        idPreference?.setOnPreferenceClickListener {
            showUserListDialog(requireContext()) // 사용자 ID 목록 다이얼로그 표시
            true
        }
    }

    override fun onPreferenceTreeClick(preference: Preference): Boolean {
        when (preference.key) {
            "open_map" -> {
                // Google Maps Activity로 이동
                val intent = Intent(requireContext(), MapsActivity::class.java)
                startActivity(intent)
                return true
            }
        }
        return super.onPreferenceTreeClick(preference)
    }

    private fun showUserListDialog(context: Context) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("사용자 정보")

        // 현재 사용자 ID 목록 표시
        val userList = userIds.joinToString("\n") { it }
        builder.setMessage(if (userList.isEmpty()) "추가된 ID가 없습니다." else userList)

        // 추가 버튼
        builder.setPositiveButton("추가") { _, _ -> showAddUserDialog(context) }
        // 삭제 버튼
        builder.setNeutralButton("삭제") { _, _ -> showDeleteUserDialog(context) }
        // 닫기 버튼
        builder.setNegativeButton("닫기", null)

        builder.show()
    }

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
                userIds.add(newUserId) // 새로운 ID 추가
            }
        }
        builder.setNegativeButton("취소", null)

        builder.show()
    }

    private fun showDeleteUserDialog(context: Context) {
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
            AlertDialog.Builder(context)
                .setTitle("ID 삭제됨")
                .setMessage("삭제된 ID: $removedId")
                .setPositiveButton("확인", null)
                .show()
        }

        builder.setNegativeButton("취소", null)
        builder.show()
    }
}
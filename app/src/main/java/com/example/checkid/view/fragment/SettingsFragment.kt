package com.example.yourpackage

import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.checkid.MapsActivity
import com.example.checkid.R
import android.content.Intent

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {

        setPreferencesFromResource(R.xml.setting_preference, rootKey)
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
}

/*Android에서 사용자 설정 화면을 구현하기 위해 사용. 이 프래그먼트는
  XML로 정의된 PreferneceScreeen을 UI로 변환하여 사용자가 쉽게 변경할 수
  있게 해준다.*/

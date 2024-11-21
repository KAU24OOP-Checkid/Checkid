package com.example.checkid.view

import android.app.NotificationChannel
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.checkid.R
import com.example.checkid.databinding.ActivityMainBinding
import com.example.checkid.model.NotificationChannelManager.createNotificationChannel
import com.example.checkid.view.fragment.EmptyFragment
import com.example.checkid.view.fragment.NotificationFragment
import com.example.checkid.view.fragment.SettingsFragment


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        val view = binding.root
        setContentView(view)

        // 권환 확인 후 요청 확인 logic 추가 위치

        createNotificationChannel(applicationContext)

        // notification을 통해 실행할 경우 NotificationFragment
        if (intent?.getStringExtra("openFragment") == "NotificationFragment")
            replaceFragment(NotificationFragment())

        // 일반적인 경우 ReportFragment
        else
            replaceFragment(EmptyFragment())


        // bottomNavigationBar 관련 logic
        binding.bottomNavigationMenu.setOnItemSelectedListener {
            item -> when (item.itemId) {
                R.id.page_home -> replaceFragment(EmptyFragment())
                R.id.page_statistics -> replaceFragment(EmptyFragment())
                R.id.page_notification -> replaceFragment(NotificationFragment())
                R.id.page_setting -> replaceFragment(SettingsFragment())
                else -> false
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) : Boolean {
        supportFragmentManager.beginTransaction()
            .setReorderingAllowed(true)
            .replace(R.id.activity_main_FragmentContainerView, fragment)
            .commit()

        return true
    }
}
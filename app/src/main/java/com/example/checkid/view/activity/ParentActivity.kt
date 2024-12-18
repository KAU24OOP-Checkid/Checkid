package com.example.checkid.view.activity

import android.os.Bundle

import com.example.checkid.R
import com.example.checkid.databinding.ActivityParentBinding
import com.example.checkid.view.fragment.ReportFragment
import com.example.checkid.view.fragment.StatisticsFragment
import com.example.checkid.view.fragment.NotificationFragment
import com.example.checkid.view.fragment.SettingsFragment

class ParentActivity : BaseActivity() {
    private lateinit var binding: ActivityParentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityParentBinding.inflate(layoutInflater)

        val view = binding.root

        setContentView(view)
        showNavigationBar()

        // Main logic
        // notification을 통해 실행할 경우 NotificationFragment
        if (intent?.getStringExtra("openFragment") == "NotificationFragment")
            replaceFragment(NotificationFragment())

        // 일반적인 경우 ReportFragment
        else
            replaceFragment(ReportFragment())

        binding.bottomNavigationMenu.setOnItemSelectedListener {
            item -> when (item.itemId) {
                R.id.page_home -> replaceFragment(ReportFragment())
                R.id.page_statistics -> replaceFragment(StatisticsFragment())
                R.id.page_notification -> replaceFragment(NotificationFragment())
                R.id.page_setting -> replaceFragment(SettingsFragment())
                else -> false
            }
        }
    }
}
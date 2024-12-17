package com.example.checkid.view

import android.os.Bundle
import androidx.fragment.app.Fragment

import com.example.checkid.R
import com.example.checkid.databinding.ActivityChildBinding
import com.example.checkid.view.fragment.ReportFragment
import com.example.checkid.view.fragment.NotificationFragment
import com.example.checkid.view.fragment.SettingsFragment


class ChildActivity : BaseActivity(){
    private lateinit var binding: ActivityChildBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChildBinding.inflate(layoutInflater)

        val view = binding.root

        setContentView(view)
        showNavigationBar()

        if (false) {

        }

        else {
            replaceFragment(ReportFragment()) // 나중에 바꾸기
        }

        binding.bottomNavigationMenu.setOnItemSelectedListener {
            item -> when (item.itemId) {
                // R.id.page_app -> replaceFragment(ReportFragment())
                // R.id.page_test -> replaceFragment(StatisticsFragment())
                R.id.page_notification -> replaceFragment(NotificationFragment())
                R.id.page_setting -> replaceFragment(SettingsFragment())
                else -> false
            }
        }
    }
}
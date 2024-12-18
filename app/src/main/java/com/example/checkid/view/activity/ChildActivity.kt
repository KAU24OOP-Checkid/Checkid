package com.example.checkid.view.activity

import android.os.Bundle

import com.example.checkid.R
import com.example.checkid.databinding.ActivityChildBinding
import com.example.checkid.view.fragment.AppFragment
import com.example.checkid.view.fragment.NotificationFragment
import com.example.checkid.view.fragment.SettingsFragment
import com.example.checkid.view.fragment.TestFragment


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
            replaceFragment(AppFragment()) // 나중에 바꾸기
        }

        binding.bottomNavigationMenu.setOnItemSelectedListener {
            item -> when (item.itemId) {
                R.id.page_app -> replaceFragment(AppFragment())
                R.id.page_test -> replaceFragment(TestFragment())
                R.id.page_notification -> replaceFragment(NotificationFragment())
                R.id.page_setting -> replaceFragment(SettingsFragment())
                else -> false
            }
        }
    }
}
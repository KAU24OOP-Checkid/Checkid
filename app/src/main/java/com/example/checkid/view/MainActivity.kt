package com.example.checkid.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.checkid.R
import com.example.checkid.databinding.ActivityMainBinding
import com.example.checkid.model.NotificationChannelManager.createNotificationChannel
import com.example.checkid.view.fragment.ReportFragment
import com.example.checkid.view.fragment.StatisticsFragment
import com.example.checkid.view.fragment.EmptyFragment
import com.example.checkid.view.fragment.NotificationFragment
import com.example.checkid.view.dialogFragment.PermissionRequestDialogFragment
import com.example.checkid.view.fragment.SettingsFragment
import com.google.firebase.database.FirebaseDatabase


class MainActivity : AppCompatActivity(), PermissionRequestDialogFragment.PermissionRequestListener {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        binding = ActivityMainBinding.inflate(layoutInflater)

        val database = FirebaseDatabase.getInstance()
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

    override fun onPermissionGranted() {

    }

    private fun replaceFragment(fragment: Fragment) : Boolean {
        supportFragmentManager.beginTransaction()
            .setReorderingAllowed(true)
            .replace(R.id.activity_main_FragmentContainerView, fragment)
            .commit()

        return true
    }
}
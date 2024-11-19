package com.example.checkid.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.checkid.R
import com.example.checkid.databinding.ActivityMainBinding
import com.example.checkid.view.fragment.EmptyFragment
import com.example.checkid.view.fragment.NotificationFragment
import com.example.checkid.view.SettingsFragment



class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        val view = binding.root
        setContentView(view)
        replaceFragment(EmptyFragment())

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
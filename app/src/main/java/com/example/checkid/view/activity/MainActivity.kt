package com.example.checkid.view.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope

import com.example.checkid.R
import com.example.checkid.databinding.ActivityMainBinding
import com.example.checkid.view.fragment.AppFragment
import com.example.checkid.view.fragment.LoginFragment
import com.example.checkid.view.fragment.NotificationFragment
import com.example.checkid.view.fragment.PermissionFragment
import com.example.checkid.view.fragment.ReportFragment
import com.example.checkid.view.fragment.SettingsFragment
import com.example.checkid.view.fragment.StatisticsFragment
import com.example.checkid.viewmodel.LoginViewModel
import com.example.checkid.viewmodel.LoginViewModelFactory
import com.example.checkid.viewmodel.PermissionViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

open class MainActivity : AppCompatActivity()  {
    private lateinit var binding: ActivityMainBinding

    private val loginViewModel: LoginViewModel by viewModels() {
        LoginViewModelFactory(applicationContext)
    }

    private val permissionViewModel: PermissionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        val view = binding.root

        setContentView(view)

        binding.bottomNavigationMenu.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.page_home -> replaceFragment(ReportFragment())
                R.id.page_statistics -> replaceFragment(StatisticsFragment())
                R.id.page_notification -> replaceFragment(NotificationFragment())
                R.id.page_setting -> replaceFragment(SettingsFragment())
            }

            true
        }

        hideNavigationBar()

        check()
    }

    fun check() {
        lifecycleScope.launch {
            val isLogin = withContext(Dispatchers.IO) { loginViewModel.isLogin(applicationContext)}

            val hasPermission = withContext(Dispatchers.IO) {permissionViewModel.checkAllPermissions(applicationContext)}

            if (!isLogin) {
                replaceFragment(LoginFragment())
            }

            else if (!hasPermission) {
                replaceFragment(PermissionFragment())
            }

            else {
                val userType = withContext(Dispatchers.IO) {loginViewModel.getUserType(applicationContext)}

                if (userType == "Parent") {
                    replaceFragment(ReportFragment())
                    setNavigationMenu(true)
                    showNavigationBar()
                }

                else {
                    replaceFragment(AppFragment())
                    setNavigationMenu(false)
                    showNavigationBar()
                }
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) : Boolean {
        supportFragmentManager.beginTransaction()
            .setReorderingAllowed(true)
            .replace(R.id.activity_main_fragmentContainerView, fragment)
            .commitNow()

        return true
    }

    private fun setNavigationMenu(isParent: Boolean) {
        val menuId = if (isParent) {
            R.menu.bottom_navigation_menu_parent
        }

        else {
            R.menu.bottom_navigation_menu_child
        }

        binding.bottomNavigationMenu.menu.clear()
        binding.bottomNavigationMenu.inflateMenu(menuId)
    }

    private fun hideNavigationBar() {
        supportActionBar?.hide()
        binding.bottomNavigationMenu.visibility = View.GONE

    }

    fun showNavigationBar() {
        supportActionBar?.show()
        binding.bottomNavigationMenu.visibility = View.VISIBLE
    }
}
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
import com.example.checkid.model.NotificationChannelManager
import com.example.checkid.view.fragment.AppFragment
import com.example.checkid.view.fragment.LoginFragment
import com.example.checkid.view.fragment.NotificationFragment
import com.example.checkid.view.fragment.PermissionFragment
import com.example.checkid.view.fragment.ReportFragment
import com.example.checkid.view.fragment.SettingsFragment
import com.example.checkid.view.fragment.StatisticsFragment
import com.example.checkid.view.fragment.TestFragment
import com.example.checkid.viewmodel.LoginViewModel
import com.example.checkid.viewmodel.LoginViewModelFactory
import com.example.checkid.viewmodel.PermissionViewModel
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
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
        FirebaseApp.initializeApp(this) // Firebase 초기화
        binding = ActivityMainBinding.inflate(layoutInflater)

        val view = binding.root

        setContentView(view)
        hideNavigationBar()

        check()
        listenFirestoreData() // Firestore 실시간 리스너 호출 로그 확인용
    }
    // Firestore에서 실시간 데이터 업데이트 확인
    private fun listenFirestoreData() {
        val db = FirebaseFirestore.getInstance()
        db.collection("TimeSettings").document("shared_time")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.e("MainActivity", "Firestore 리스너 오류: ${e.message}")
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    val time = snapshot.getString("selected_time")
                    Log.d("MainActivity", "Firestore에서 업데이트된 시간: $time")
                } else {
                    Log.d("MainActivity", "Firestore 문서가 존재하지 않습니다.")
                }
            }
    }

    fun check() {
        lifecycleScope.launch {
            val isLogin = withContext(Dispatchers.IO) { loginViewModel.isLogin(applicationContext) }

            val hasPermission = withContext(Dispatchers.IO) { permissionViewModel.checkAllPermissions(applicationContext) }

            if (!isLogin) {
                replaceFragment(LoginFragment())
            }

            else if (!hasPermission) {
                replaceFragment(PermissionFragment())
            }

            else {
                val userType = withContext(Dispatchers.IO) {loginViewModel.getUserType(applicationContext)}

                NotificationChannelManager.createNotificationChannel(applicationContext)
                bindNavigationMenu(userType)
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

    private fun bindNavigationMenu(userType: String) {
        val menuId = if(userType == "Parent") {
            R.menu.bottom_navigation_menu_parent
        }

        else {
            R.menu.bottom_navigation_menu_child
        }

        binding.bottomNavigationMenu.menu.clear()
        binding.bottomNavigationMenu.inflateMenu(menuId)

        if (userType == "Parent") {
            binding.bottomNavigationMenu.setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.page_report -> replaceFragment(ReportFragment())
                    R.id.page_statistics -> replaceFragment(StatisticsFragment())
                    R.id.page_notification -> replaceFragment(NotificationFragment())
                    R.id.page_setting -> replaceFragment(SettingsFragment())
                }

                true
            }

            replaceFragment(ReportFragment())
        }

        else {
            binding.bottomNavigationMenu.setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.page_app -> replaceFragment(AppFragment())
                    R.id.page_test -> replaceFragment(TestFragment())
                    R.id.page_notification -> replaceFragment(NotificationFragment())
                    R.id.page_setting -> replaceFragment(SettingsFragment())
                }

                true
            }

            replaceFragment(AppFragment())
        }

        showNavigationBar()
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
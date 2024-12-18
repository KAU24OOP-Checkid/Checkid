package com.example.checkid.view.activity

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager

import com.example.checkid.R
import com.example.checkid.databinding.ActivityBaseBinding
import com.example.checkid.receiver.MyReceiver
import com.example.checkid.view.fragment.LoginFragment
import com.example.checkid.view.fragment.PermissionFragment
import com.example.checkid.viewmodel.LoginViewModel
import com.example.checkid.viewmodel.LoginViewModelFactory
import com.example.checkid.viewmodel.PermissionViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

open class BaseActivity : AppCompatActivity()  {
    private lateinit var binding: ActivityBaseBinding

    private val loginViewModel: LoginViewModel by viewModels() {
        LoginViewModelFactory(applicationContext)
    }
    private val permissionViewModel: PermissionViewModel by viewModels()

    private val receiver = MyReceiver { action ->
        when (action) {
            "LOGIN_SUCCESS" -> {
                check() // LOGIN_SUCCESS 처리
            }
            "PERMISSION_SUCCESS" -> {
                check() // PERMISSION_SUCCESS 처리
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBaseBinding.inflate(layoutInflater)

        val view = binding.root

        setContentView(view)
        hideNavigationBar()

        val intentFilter = IntentFilter().apply {
            addAction("LOGIN_SUCCESS")
            addAction("PERMISSION_SUCCESS")
        }

        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, intentFilter)

        check() // 마지막에 있어야 한다.
    }

    override fun onDestroy() {
        super.onDestroy()

        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver)
    }

    private fun check() {
        lifecycleScope.launch {
            val isLogin = withContext(Dispatchers.IO) {
                loginViewModel.isLogin(applicationContext)
            }

            val hasPermission = withContext(Dispatchers.IO) {
                permissionViewModel.checkAllPermissions(applicationContext)
            }

            withContext(Dispatchers.Main) {
                when {
                    !isLogin -> replaceFragment(LoginFragment())
                    !hasPermission -> replaceFragment(PermissionFragment())
                    else -> swapActivity()
                }
            }

        }
    }

    private suspend fun swapActivity() {
        val userType = withContext(Dispatchers.IO) {
            loginViewModel.getUserType(applicationContext)
        }

        val intent = when (userType) {
            "Parent" -> Intent(this, ParentActivity::class.java)
            "Child" -> Intent(this, ChildActivity::class.java)
            else -> null
        }

        intent?.let {
            startActivity(it)
            finish() // 현재 Activity 종료
        } ?: finish()
    }

    protected fun replaceFragment(fragment: Fragment) : Boolean {
        supportFragmentManager.beginTransaction()
            .setReorderingAllowed(true)
            .replace(R.id.activity_base_FragmentContainerView, fragment)
            .commit()

        return true
    }

    protected fun hideNavigationBar() {
        supportActionBar?.hide()
    }

    protected fun showNavigationBar() {
        supportActionBar?.show()
    }
}
package com.example.checkid.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope

import com.example.checkid.R
import com.example.checkid.databinding.ActivityBaseBinding
import com.example.checkid.model.DataStoreManager
import com.example.checkid.view.fragment.LoginFragment
import com.example.checkid.view.fragment.PermissionFragment
import com.example.checkid.viewmodel.LoginViewModel
import com.example.checkid.viewmodel.LoginViewModelFactory
import com.example.checkid.viewmodel.PermissionViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

open class BaseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBaseBinding

    private val loginViewModel: LoginViewModel by viewModels() {
        LoginViewModelFactory(applicationContext)
    }
    private val permissionViewModel: PermissionViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBaseBinding.inflate(layoutInflater)

        val view = binding.root

        setContentView(view)
        hideNavigationBar()

        startActivity()

        /*
        lifecycleScope.launch {
            if (login()) {
                if (permission()) {
                    startActivity()
                }
            }
        }

         */
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun login(): Boolean {
        if (loginViewModel.isLogin(applicationContext)) {
            return true
        }

        return suspendCancellableCoroutine { continuation ->
            replaceFragment(LoginFragment())

            lifecycleScope.launch {
                val isLoggedIn = loginViewModel.loginResult.first {it}
                closeFragment()
                continuation.resume(true)
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun permission():Boolean {
        if(!permissionViewModel.checkAllPermissions(applicationContext)) {
            return true
        }

        return suspendCancellableCoroutine { continuation ->
            replaceFragment(PermissionFragment())

            lifecycleScope.launch {
                val isGranted = permissionViewModel.permissionResult.first {it}
                closeFragment()
                continuation.resume(true)
            }
        }
    }

    private fun startActivity():Boolean {
        val userType = DataStoreManager.getUserTypeSync(applicationContext)

        val intent = when (userType) {
            "Parent" -> Intent(this, ParentActivity::class.java)
            "Child" -> Intent(this, ChildActivity::class.java)
            else -> null
        }

        intent?.let {
            startActivity(it)
            finish() // 현재 Activity 종료
        } ?: run {
            // 유저 타입이 비정상적일 경우 로그 또는 처리
            println("User type is invalid or null")
        }

        finish()

        return true
    }

    protected fun replaceFragment(fragment: Fragment) : Boolean {
        supportFragmentManager.beginTransaction()
            .setReorderingAllowed(true)
            .replace(R.id.activity_base_FragmentContainerView, fragment)
            .commit()

        return true
    }

    private fun closeFragment() {
        supportFragmentManager.findFragmentById(R.id.activity_base_FragmentContainerView)?.let {
            supportFragmentManager.beginTransaction().remove(it).commit()
        }
    }

    protected fun hideNavigationBar() {
        supportActionBar?.hide()
    }

    protected fun showNavigationBar() {
        supportActionBar?.show()
    }
}
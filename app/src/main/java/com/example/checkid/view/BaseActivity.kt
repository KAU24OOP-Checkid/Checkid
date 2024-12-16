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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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

        lifecycleScope.launch {
            login()
            permission()
            startActivity()
        }
    }

    private suspend fun login() {
        if(!loginViewModel.isLogin(applicationContext)) {
            replaceFragment(LoginFragment())

            waitUntil {loginViewModel.isLogin(applicationContext)}
        }
    }

    private suspend fun permission() {
        if(!permissionViewModel.checkAllPermissions(applicationContext)) {
            replaceFragment(PermissionFragment())

            waitUntil {permissionViewModel.checkAllPermissions(applicationContext)}
        }
    }

    private suspend fun startActivity() {
        val userType = DataStoreManager.getUserType(applicationContext)
        val intent = when (userType) {
            "Parent" -> Intent(this, ParentActivity::class.java)
            "Child" -> Intent(this, ChildActivity::class.java)
            else -> null
        }

        if (intent != null) {
            startActivity(intent)
        }

        finish()
    }

    private suspend fun waitUntil(condition: () -> Boolean) {
        while (!condition()) {
            delay(100000)
        }
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
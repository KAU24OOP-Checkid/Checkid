package com.example.checkid.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

import com.example.checkid.R
import com.example.checkid.databinding.ActivityMainBinding
import com.example.checkid.model.DataStoreManager
import com.example.checkid.model.NotificationChannelManager.createNotificationChannel
import com.example.checkid.view.dialogFragment.PermissionRequestDialogFragment
import com.example.checkid.view.fragment.LoginFragment
import com.example.checkid.viewmodel.LoginViewModel
import com.example.checkid.viewmodel.LoginViewModelFactory
import com.google.firebase.Firebase
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class LoginActivity : AppCompatActivity(), PermissionRequestDialogFragment.PermissionRequestListener {
    private lateinit var binding: ActivityMainBinding

    private val loginViewModel: LoginViewModel by viewModels() {
        LoginViewModelFactory(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        binding = ActivityMainBinding.inflate(layoutInflater)

        val view = binding.root

        setContentView(view)
        supportActionBar?.hide()
        binding.bottomNavigationMenu.visibility = View.GONE


        // Application 사용 사전 준비 logic
        login { // 로그인 Check
            permission { // 권한 Check
                createNotificationChannel(applicationContext)

                val userType = DataStoreManager.getUserType(applicationContext)

                when (userType) {
                    "Parent" -> ParentActivity::class.java
                    "Child" -> ChildActivity::class.java
                    else -> null
                }?.let {
                    startActivity(Intent(this, it))
                }

                finish()
            }
        }
    }

    private fun login(onLoginComplete: () -> Unit) {
        loginViewModel.isLogin.observe(this) { isLogin ->
            if (!isLogin) {
                supportFragmentManager.beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.activity_main_FragmentContainerView, LoginFragment())
                    .commit()
            }

            else {
                onLoginComplete()
            }
        }
    }

    private fun permission(onPermissionComplete: () -> Unit) {
       onPermissionComplete()
    }

    override fun onPermissionGranted() {

    }
}
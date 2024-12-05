package com.example.checkid.viewmodel

import User
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.checkid.model.DataStoreManager
import com.example.checkid.model.UserRepository
import kotlinx.coroutines.launch

class LoginViewModel(context: Context) : ViewModel() {
    fun toggleIsLogin(context: Context) {
        val isLogin = DataStoreManager.getIsLogin(context)

        viewModelScope.launch {
            DataStoreManager.toggleIsLogin(context, isLogin)
        }
    }

    fun isLogin(context: Context): Boolean {
        return DataStoreManager.getIsLogin(context)
    }

    suspend fun login(context: Context, id: String, pw: String) : Boolean {
        val user: User? = UserRepository.findByIdPw(id, pw)

        if (user != null) {
            DataStoreManager.setUserId(context, id)
            DataStoreManager.setUserPartnerId(context, user.partner_id)

            return true
        }

        return false
    }
}

class LoginViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
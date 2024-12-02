package com.example.checkid.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.checkid.model.DataStoreManager
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
}

class LoginViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
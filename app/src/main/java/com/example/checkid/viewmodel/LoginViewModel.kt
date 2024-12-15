package com.example.checkid.viewmodel

import User
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.checkid.model.DataStoreManager
import com.example.checkid.model.UserRepository
import kotlinx.coroutines.launch

class LoginViewModel(context: Context) : ViewModel() {
    private val _isLogin = MutableLiveData<Boolean>()
    val isLogin: LiveData<Boolean> get() = _isLogin

    init {
        viewModelScope.launch {
            _isLogin.value = DataStoreManager.getIsLogin(context)
        }
    }

    fun getUserType(context: Context): String {
        return DataStoreManager.getUserType(context)
    }

    fun isLogin(context: Context): Boolean = DataStoreManager.getIsLogin(context)

    fun login(context: Context, id: String, pw: String): Boolean {
        val user: User? = UserRepository.findByIdPw(id, pw)

        if (user != null) {
            viewModelScope.launch {
                DataStoreManager.setIsLogin(context, true)
                DataStoreManager.setUserId(context, id)
                DataStoreManager.setUserPartnerId(context, user.partner_id!!) // !! 고치기
            }

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
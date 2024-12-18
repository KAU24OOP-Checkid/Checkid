package com.example.checkid.viewmodel

import User
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.checkid.model.DataStoreManager
import com.example.checkid.model.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(context: Context) : ViewModel() {
    private val _loginResult = MutableStateFlow(false)
    val loginResult: StateFlow<Boolean> get() = _loginResult

    suspend fun getUserType(context: Context): String {
        return DataStoreManager.getUserType(context)
    }

    fun isLogin(context: Context): Boolean {
        _loginResult.value = DataStoreManager.getIsLogin(context)

        return DataStoreManager.getIsLogin(context)
    }


    fun login(context: Context, id: String, password: String) {
        viewModelScope.launch {
            val user: User? = UserRepository.getUserByIdAndPassword(id, password)

            if (user != null) {
                val userType = UserRepository.getUserType(user) ?: "Child"

                DataStoreManager.setIsLogin(context, true)
                DataStoreManager.setUserType(context, userType)
                DataStoreManager.setUserId(context, id)
                DataStoreManager.setUserPartnerId(context, user.partnerId ?: "")

                _loginResult.value = true
            }

            else {
                _loginResult.value = false
            }
        }
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
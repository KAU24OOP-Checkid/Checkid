package com.example.checkid.viewmodel

import ParentUser
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.checkid.model.DataStoreManager
import com.example.checkid.model.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class LoginViewModel(context: Context) : ViewModel() {
    private val _loginResult = MutableLiveData<Boolean?>(null)
    val loginResult: LiveData<Boolean?> get() = _loginResult

    suspend fun getUserType(context: Context): String {
        return DataStoreManager.getUserType(context)
    }

    fun isLogin(context: Context): Boolean {
        return runBlocking(Dispatchers.IO) {
            DataStoreManager.getIsLogin(context)
        }
    }

    fun login(context: Context, id: String, password: String) {
        viewModelScope.launch {
            val user = withContext(Dispatchers.IO) {
                UserRepository.getUserByIdAndPassword(id, password)
            }

            Log.d("ViewModel", "${user is ParentUser}}")

            if (user != null) {
                val userType = UserRepository.getUserType(user) ?: "Child"

                withContext(Dispatchers.IO) {
                    DataStoreManager.setIsLogin(context, true)
                    DataStoreManager.setUserType(context, userType)
                    DataStoreManager.setUserId(context, id)
                    DataStoreManager.setUserPartnerId(context, user.partnerId ?: "")
                }

                _loginResult.postValue(true)
            } else {
                _loginResult.postValue(false)
            }
        }
    }
}

class LoginViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(context) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class : ${modelClass.name}")
    }
}
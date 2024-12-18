package com.example.checkid.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.checkid.model.DataStoreManager
import com.example.checkid.model.Location
import com.example.checkid.model.LocationRepository
import com.example.checkid.model.TimeSettingRepository
import com.example.checkid.model.UserRepository
import kotlinx.coroutines.launch

class SettingsViewModel(context: Context) : ViewModel() {

    private val _location = MutableLiveData<Location?>()
    val location: LiveData<Location?> get() = _location

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    private val _selectedTime = MutableLiveData<String?>()
    val selectedTime: LiveData<String?> get() = _selectedTime

    // LiveData for ParentUser PartnerId
    private val _partnerId = MutableLiveData<String?>()
    val partnerId: LiveData<String?> get() = _partnerId

    private val _userId = MutableLiveData<String>()
    val userId: LiveData<String> get() = _userId

    suspend fun fetchUserId(context: Context) {
        val id = DataStoreManager.getUserId(context)
        val user = UserRepository.getUserById(id)

        _userId.postValue(user?.id)
    }

    suspend fun fetchPartnerUserId(context: Context) {
        val id = DataStoreManager.getUserId(context)
        val user = UserRepository.getUserById(id)

        Log.d("SettingsViewModel", "${user?.partnerId}")
        _partnerId.postValue(user?.partnerId)
    }

    fun fetchChildLocation(childId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val loc = LocationRepository.getLocationById(childId)
            if (loc != null) {
                _location.value = loc
                _errorMessage.value = null
            } else {
                _errorMessage.value = "위치 정보를 불러올 수 없습니다."
            }
            _isLoading.value = false
        }
    }

    fun setSelectedTime(time: String) {
        viewModelScope.launch {
            // Firestore에 시간 설정 저장
            val success = TimeSettingRepository.saveTimeSetting(time)
            if (success) {
                _selectedTime.value = time
            }
        }
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }

    // Firestore 실시간 리스너 설정
    fun listenToTimeSetting() {
        viewModelScope.launch {
            TimeSettingRepository.listenTimeSetting { time ->
                if (time != null) {
                    _selectedTime.postValue(time)
                } else {
                    _errorMessage.postValue("시간 정보를 불러올 수 없습니다.")
                }

            }
        }
    }
}

class SettingsViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            return SettingsViewModel(context) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class : ${modelClass.name}")
    }
}

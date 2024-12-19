package com.example.checkid.model

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

object DataStoreManager {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "system")

    private val KEY_USER_LOGIN = intPreferencesKey("key_user_login")
    private val KEY_USER_TYPE = stringPreferencesKey("key_user_type")
    private val KEY_USER_ID = stringPreferencesKey("key_id")
    private val KEY_USER_PARTNER_ID = stringPreferencesKey("key_partner_id")

    /**
     * 로그인 상태 저장
     */
    suspend fun setIsLogin(context: Context, isLogin: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[KEY_USER_LOGIN] = if (isLogin) 1 else 0
        }
    }

    /**
     * 로그인 상태 가져오기
     */
    suspend fun getIsLogin(context: Context): Boolean {
        return context.dataStore.data.map { preferences ->
            (preferences[KEY_USER_LOGIN] ?: 0) == 1
        }.first()
    }

    /**
     * 사용자 ID 저장
     */
    suspend fun setUserId(context: Context, id: String) {
        context.dataStore.edit { preferences ->
            preferences[KEY_USER_ID] = id
        }
    }

    /**
     * 사용자 ID 가져오기
     */
    suspend fun getUserId(context: Context): String {
        return context.dataStore.data.map { preferences ->
            preferences[KEY_USER_ID] ?: ""
        }.first()
    }

    /**
     * 사용자 파트너 ID 저장
     */
    suspend fun setUserPartnerId(context: Context, id: String) {
        context.dataStore.edit { preferences ->
            preferences[KEY_USER_PARTNER_ID] = id
        }
    }

    /**
     * 사용자 파트너 ID 가져오기
     */
    suspend fun getUserPartnerId(context: Context): String {
        return context.dataStore.data.map { preferences ->
            preferences[KEY_USER_PARTNER_ID] ?: ""
        }.first()
    }

    /**
     * 사용자 유형 저장
     */
    suspend fun setUserType(context: Context, userType: String) {
        context.dataStore.edit { preferences ->
            preferences[KEY_USER_TYPE] = userType
        }
    }

    /**
     * 사용자 유형 가져오기
     */
    suspend fun getUserType(context: Context): String {
        return context.dataStore.data.map { preferences ->
            preferences[KEY_USER_TYPE] ?: ""
        }.first()
    }
}

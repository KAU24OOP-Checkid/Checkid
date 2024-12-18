package com.example.checkid.model

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

object DataStoreManager {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "system")

    private val KEY_USER_LOGIN = intPreferencesKey("key_user_login")
    private val KEY_USER_TYPE = stringPreferencesKey("key_user_type")
    private val KEY_USER_ID = stringPreferencesKey("key_id")
    private val KEY_USER_PARTNER_ID = stringPreferencesKey("key_partner_id")

    suspend fun setIsLogin(context: Context, isLogin: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[KEY_USER_LOGIN] = if (isLogin) 1 else 0
        }
    }

    fun getIsLogin(context: Context): Boolean {
        return runBlocking {
            context.dataStore.data.map { preferences ->
                (preferences[KEY_USER_LOGIN] ?: 0) == 1
            }.first()
        }
    }

    suspend fun setUserId(context: Context, id: String) {
        context.dataStore.edit { preferences ->
            preferences[KEY_USER_ID] = id
        }
    }

    fun getUserId(context: Context): String {
        return runBlocking {
            context.dataStore.data.map { preferences ->
                preferences[KEY_USER_ID] ?: ""
            }.first()
        }
    }

    suspend fun setUserPartnerId(context: Context, id: String) {
        context.dataStore.edit { preferences ->
            preferences[KEY_USER_PARTNER_ID] = id
        }
    }

    fun getUserPartnerId(context: Context): String {
        return runBlocking {
            context.dataStore.data.map { preferences ->
                preferences[KEY_USER_PARTNER_ID] ?: ""
            }.first()
        }
    }

    suspend fun setUserType(context: Context, userType: String) {
        context.dataStore.edit { preferences ->
            preferences[KEY_USER_TYPE] = if (userType == "Parent") "Parent" else "Child"
        }
    }

    fun getUserTypeSync(context: Context): String? {
        return runBlocking {
            context.dataStore.data.firstOrNull()?.get(KEY_USER_TYPE)
        }
    }

    suspend fun getUserType(context: Context): String {
        val preferences = context.dataStore.data.first()
        return preferences[KEY_USER_TYPE] ?: "Parent"
    }
}

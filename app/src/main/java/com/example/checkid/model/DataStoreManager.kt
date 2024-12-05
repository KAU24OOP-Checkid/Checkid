package com.example.checkid.model

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

object DataStoreManager {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "system")

    private val KEY_USER_LOGIN = intPreferencesKey("key_user_login")
    private val KEY_USER_ID = stringPreferencesKey("key_id")
    private val KEY_USER_PARTNER_ID = stringPreferencesKey("key_partner_id")

    suspend fun toggleIsLogin(context: Context, isLogin: Boolean) {
        if (isLogin) {
            context.dataStore.edit {
                    preferences -> preferences[KEY_USER_LOGIN] = 0
            }
        }

        else {
            context.dataStore.edit {
                preferences -> preferences[KEY_USER_LOGIN] = 1
            }
        }
    }

    private fun getIsLoginFlow(context: Context) : Flow<Boolean> {
        return context.dataStore.data.map {
            preferences -> (preferences[KEY_USER_LOGIN] ?: 0) == 1
        }
    }

    fun getIsLogin(context: Context): Boolean {
        return runBlocking {
            getIsLoginFlow(context).first()
        }
    }

    suspend fun setUserId(context: Context, id: String) {
        context.dataStore.edit {
            preferences -> preferences[KEY_USER_ID] = id
        }
    }

    private fun getUserIdFlow(context: Context) : Flow<String> {
        return context.dataStore.data.map {
            preferences -> preferences[KEY_USER_ID] ?: ""
        }
    }

    fun getUserId(context: Context) : String {
        return runBlocking {
            getUserIdFlow(context).first()
        }
    }

    suspend fun setUserPartnerId(context: Context, id: String) {
        context.dataStore.edit {
            preferences -> preferences[KEY_USER_PARTNER_ID] = id
        }
    }

    fun getUserPartnerId(context: Context) :Flow<String> {
        return context.dataStore.data.map {
            preferences -> preferences[KEY_USER_PARTNER_ID] ?: ""
        }
    }
}


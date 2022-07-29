package com.dimas.coderstory.data.repo

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.dimas.coderstory.data.result.Result
import com.dimas.coderstory.data.response.LoginResponse
import com.dimas.coderstory.data.response.RegisterResponse
import com.dimas.coderstory.data.retrofit.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.lang.Exception

class UserRepo private constructor(
    private val dataStore: DataStore<Preferences>,
    private val apiService: ApiService
){

    fun register(name: String, email: String, password: String) : LiveData<Result<RegisterResponse>> = liveData {
        emit(Result.Loading)
        try{
            val result = apiService.register(name, email, password)
            emit(Result.Success(result))
        }catch (e : Exception)
        {
            e.printStackTrace()
            emit(Result.Error(e.message.toString()))
        }

    }
    fun login(email: String, password: String) : LiveData<Result<LoginResponse>> = liveData {
        emit(Result.Loading)
        try {
            val result = apiService.login(email, password)
            emit(Result.Success(result))
        }catch (e: Exception)
        {
            e.printStackTrace()
            emit(Result.Error(e.message.toString()))
        }
    }



    fun getToken() : Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[TOKEN] ?: ""
        }
    }


    fun isLogin() : Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[STATE_KEY] ?: false
        }
    }

    suspend fun setToken(token: String, isLogin: Boolean) {
        dataStore.edit { preferences ->
            preferences[TOKEN] = token
            preferences[STATE_KEY] = isLogin
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences[TOKEN] = ""
            preferences[STATE_KEY] = false
        }
    }

    private val USER_NAME_KEY = stringPreferencesKey("user_name")
    /**
     * User Name
     */

    fun getUserName(): Flow<String> = dataStore.data.map {
        it[USER_NAME_KEY] ?: DEFAULT_VALUE
    }

    suspend fun saveUserName(name: String) {
        dataStore.edit {
            it[USER_NAME_KEY] = name
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserRepo? = null
        const val DEFAULT_VALUE = "Coder"

        private val TOKEN = stringPreferencesKey("token")
        private val STATE_KEY = booleanPreferencesKey("state")

        fun getInstance(
            dataStore: DataStore<Preferences>,
            apiService: ApiService
        ): UserRepo {
            return INSTANCE ?: synchronized(this) {
                val instance = UserRepo(dataStore, apiService)
                INSTANCE = instance
                instance
            }
        }
    }
}
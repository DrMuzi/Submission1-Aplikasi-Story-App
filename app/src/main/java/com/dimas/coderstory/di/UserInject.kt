package com.dimas.coderstory.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.dimas.coderstory.data.repo.UserRepo
import com.dimas.coderstory.data.retrofit.ApiConfig

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

object UserInject {
    fun provideRepository(context: Context): UserRepo {
        val apiService = ApiConfig.getApiService()
        return UserRepo.getInstance(context.dataStore, apiService)
    }
}
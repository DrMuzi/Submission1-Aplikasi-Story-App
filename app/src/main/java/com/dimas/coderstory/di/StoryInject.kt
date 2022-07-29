package com.dimas.coderstory.di

import com.dimas.coderstory.data.repo.StoryRepo
import com.dimas.coderstory.data.retrofit.ApiConfig


object StoryInject {
    fun provideRepository(): StoryRepo {
        val apiService = ApiConfig.getApiService()
        return StoryRepo.getInstance(apiService)
    }
}
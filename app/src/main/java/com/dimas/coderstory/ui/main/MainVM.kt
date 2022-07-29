package com.dimas.coderstory.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dimas.coderstory.data.repo.StoryRepo
import com.dimas.coderstory.data.repo.UserRepo
import kotlinx.coroutines.launch

class MainVM(private val userRepo: UserRepo, private val storyRepo: StoryRepo) : ViewModel() {

    fun getToken() : LiveData<String> {
        return userRepo.getToken().asLiveData()
    }

    fun isLogin() : LiveData<Boolean> {
        return userRepo.isLogin().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            userRepo.logout()
        }
    }

    fun getStories(token: String) = storyRepo.getStories(token)

    fun getName() : LiveData<String> = userRepo.getUserName().asLiveData()
    suspend fun saveUserToken(value: String) = userRepo.saveUserName(value)
}
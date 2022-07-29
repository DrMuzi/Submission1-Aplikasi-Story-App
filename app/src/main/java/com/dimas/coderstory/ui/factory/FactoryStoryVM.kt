package com.dimas.coderstory.ui.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dimas.coderstory.data.repo.StoryRepo
import com.dimas.coderstory.data.repo.UserRepo
import com.dimas.coderstory.di.StoryInject
import com.dimas.coderstory.di.UserInject
import com.dimas.coderstory.ui.main.MainVM
import com.dimas.coderstory.ui.story.StoryVM

class FactoryStoryVM private constructor(private val userRepo: UserRepo, private val storyRepo: StoryRepo) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainVM::class.java) -> {
                MainVM(userRepo, storyRepo) as T
            }
            modelClass.isAssignableFrom(StoryVM::class.java) -> {
                StoryVM(userRepo, storyRepo) as T
            }
            else -> {
                throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
            }
        }
    }

    companion object {
        @Volatile
        private var instance: FactoryStoryVM? = null
        fun getInstance(context: Context): FactoryStoryVM =
            instance ?: synchronized(this) {
                instance ?: FactoryStoryVM(UserInject.provideRepository(context), StoryInject.provideRepository())
            }.also { instance = it }
    }
}
package com.dimas.coderstory.ui.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dimas.coderstory.data.repo.UserRepo
import com.dimas.coderstory.di.UserInject
import com.dimas.coderstory.ui.login.LoginVM
import com.dimas.coderstory.ui.register.SignupViewModel

class FactoryUserVM(private val userRepo: UserRepo) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(SignupViewModel::class.java) -> {
                SignupViewModel(userRepo) as T
            }
            modelClass.isAssignableFrom(LoginVM::class.java) -> {
                LoginVM(userRepo) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var instance: FactoryUserVM? = null
        fun getInstance(context: Context): FactoryUserVM =
            instance ?: synchronized(this) {
                instance ?: FactoryUserVM(UserInject.provideRepository(context))
            }.also { instance = it }
    }
}
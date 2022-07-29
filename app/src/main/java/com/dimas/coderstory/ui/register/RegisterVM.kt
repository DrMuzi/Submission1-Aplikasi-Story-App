package com.dimas.coderstory.ui.register

import androidx.lifecycle.ViewModel
import com.dimas.coderstory.data.repo.UserRepo

class SignupViewModel(private val repo: UserRepo) : ViewModel() {

    fun register(name: String, email: String, password: String) = repo.register(name, email, password)
}
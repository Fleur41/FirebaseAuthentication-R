package com.sam.firebaseauthentication_r.authentication.signup

import javax.inject.Inject
import dagger.hilt.android.lifecycle.HiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sam.firebaseauthentication_r.authentication.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository
): ViewModel() {
    fun signUp(email: String, password: String){
        viewModelScope.launch(Dispatchers.IO) {
            repository.signUp(email, password)
        }
    }
    fun signIn(email: String, password: String){
        viewModelScope.launch(Dispatchers.IO) {
            repository.signIn(email, password)
        }
    }
}
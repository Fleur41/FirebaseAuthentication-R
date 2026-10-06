package com.sam.firebaseauthentication_r.authentication.signup


import android.util.Log
import javax.inject.Inject
import dagger.hilt.android.lifecycle.HiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sam.firebaseauthentication_r.authentication.AuthRepository
import com.sam.firebaseauthentication_r.datastore.DatastoreRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val datastoreRepository: DatastoreRepository
): ViewModel() {
    private val _authState = MutableStateFlow<AuthState>(AuthState.Initial)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()
    
//    init {
//        Log.d("TAG", "Created an instance of ${this::class.simpleName}")
//    }
    fun signUp(email: String, password: String){
        viewModelScope.launch(Dispatchers.IO) {
            _authState.value = AuthState.Loading
            delay(3.seconds)
            repository.signUp(
                email = email,
                password = password,
                onSignUpSuccess = {
                    saveIsAuthenticated(true)
                    _authState.value = AuthState.Success
                },
                onSignUpFailure = { exception ->
                    _authState.value = AuthState.Error(exception.message ?: "Unknown error")
                }
            )
        }
    }
    fun signIn(email: String, password: String){
        viewModelScope.launch(Dispatchers.IO) {
            _authState.value = AuthState.Loading
            delay(3.seconds)
            repository.signIn(
                email = email,
                password = password,
                onSuccess = {
                    saveIsAuthenticated(true)
                    _authState.value = AuthState.Success
                },
                onFailure = { exception ->
                    _authState.value = AuthState.Error(exception.message ?: "Unknown error")
                }
            )
        }
    }

    // password reset
    fun sendPasswordResetEmail(email: String){
        viewModelScope.launch(Dispatchers.IO) {
            _authState.value = AuthState.Loading
            repository.sendPasswordResetEmail(
                email = email,
                onSuccess = {
                    _authState.value = AuthState.Success
                },
                onFailure = { exception ->
                    _authState.value = AuthState.Error(exception.message ?: "Failed to send reset email")
                }
            )
        }
    }

    // ADDED: Google Sign-In method
    fun signInWithGoogle(idToken: String){
        viewModelScope.launch(Dispatchers.IO) {
            _authState.value = AuthState.Loading
            repository.signInWithGoogle(
                idToken = idToken,
                onSuccess = {
                    saveIsAuthenticated(true)
                    _authState.value = AuthState.Success
                },
                onFailure = {exception ->
                    _authState.value = AuthState.Error(exception.message ?: "Google Sign-In failed")
                }
            )
        }
    }

    // In real app, we don't need this one here as it is taken care of in the AuthRepository.
    fun signOut() {
        repository.signOut()
        saveIsAuthenticated(false)
        _authState.value = AuthState.Initial
    }

    fun saveIsAuthenticated(authenticated: Boolean){
        viewModelScope.launch {
            datastoreRepository.saveIsAuthenticated(authenticated)
        }
    }

    override fun onCleared() {
        Log.d("TAG", "Clearing an instance of ${this::class.simpleName}")
        super.onCleared()
    }
}

sealed interface AuthState{
    data object Initial: AuthState
    data object Loading: AuthState
    data object Success: AuthState
    data class Error(val message: String): AuthState
}
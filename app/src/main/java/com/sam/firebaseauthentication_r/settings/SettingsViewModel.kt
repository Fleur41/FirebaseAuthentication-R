package com.sam.firebaseauthentication_r.settings

import javax.inject.Inject
import dagger.hilt.android.lifecycle.HiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sam.firebaseauthentication_r.authentication.AuthRepository
import com.sam.firebaseauthentication_r.datastore.DatastoreRepository
import com.sam.firebaseauthentication_r.navigation.NavigationDestination
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private  val datastoreRepository: DatastoreRepository
): ViewModel() {
    private val _startDestination = MutableStateFlow<NavigationDestination>(NavigationDestination.Splash)
    val startDestination: StateFlow<NavigationDestination> = _startDestination.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            delay(1.seconds)
            datastoreRepository.authenticated.collect { authenticated ->
                _startDestination.value = if (authenticated) {
                    NavigationDestination.Home
                } else {
                    NavigationDestination.SignIn
                }
            }
        }
    }
    val authenticated = datastoreRepository.authenticated.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = false
    )

    fun saveIsAuthenticated(authenticated: Boolean){
        viewModelScope.launch {
            datastoreRepository.saveIsAuthenticated(authenticated)
        }
    }

    fun logout(){
        viewModelScope.launch {
            authRepository.signOut()
            datastoreRepository.saveIsAuthenticated(false)
        }

    }
}
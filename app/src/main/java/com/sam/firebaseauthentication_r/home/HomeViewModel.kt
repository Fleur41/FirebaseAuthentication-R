package com.sam.firebaseauthentication_r.home

import javax.inject.Inject
import dagger.hilt.android.lifecycle.HiltViewModel
import androidx.lifecycle.ViewModel

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository
): ViewModel() {
    fun logDetailScreenViewEvent(){
        homeRepository.logDetailScreenView()
    }
}
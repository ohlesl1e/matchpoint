package com.lesliezhou.matchpoint.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lesliezhou.matchpoint.HomeActivity

class ProfileViewModelFactory( private val activity: HomeActivity?): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)){
            return ProfileViewModel(activity) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}
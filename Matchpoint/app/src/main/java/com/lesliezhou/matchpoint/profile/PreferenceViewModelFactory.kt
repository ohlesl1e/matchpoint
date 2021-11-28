package com.lesliezhou.matchpoint.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lesliezhou.matchpoint.HomeActivity

class PreferenceViewModelFactory(
    private val activity: HomeActivity?
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PreferenceViewModel::class.java)) {
            return PreferenceViewModel(activity) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}
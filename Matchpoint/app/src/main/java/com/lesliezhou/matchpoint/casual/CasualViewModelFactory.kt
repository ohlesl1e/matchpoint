package com.lesliezhou.matchpoint.casual

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lesliezhou.matchpoint.HomeActivity
import java.lang.IllegalArgumentException

class CasualViewModelFactory(private val activity: HomeActivity?): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CasualViewModel::class.java)){
            return CasualViewModel(activity) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}
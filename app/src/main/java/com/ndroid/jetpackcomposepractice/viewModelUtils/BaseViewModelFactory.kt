package com.ndroid.jetpackcomposepractice.viewModelUtils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


/**
 * Created by Nityen on 26-02-2025.
 */

class BaseViewModelFactory<T : ViewModel>(private val creator: () -> T) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return creator() as T
    }
}
package com.ndroid.jetpackcomposepractice.viewModelUtils

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel


/**
 * Created by Nityen on 26-02-2025.
 */


@Composable
inline fun <reified T : ViewModel> getViewModel(
    noinline creator: (() -> T)? = null
): T {
    val factory = if (creator != null) {
        BaseViewModelFactory(creator)
    } else {
        null
    }
    return viewModel(factory = factory)
}
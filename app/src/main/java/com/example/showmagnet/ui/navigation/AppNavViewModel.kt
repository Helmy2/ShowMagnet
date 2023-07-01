package com.example.showmagnet.ui.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.showmagnet.domain.use_case.user.IsUserSingedInUserCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class AppNavViewModel
@Inject constructor(
    isUserSingedInUserCase: IsUserSingedInUserCase
) : ViewModel() {
    val isUserSingedIn = isUserSingedInUserCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )
}
package com.example.showmagnet.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.showmagnet.domain.use_case.user.GetUserInfoUserCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getUserInfoUserCase: GetUserInfoUserCase
) : ViewModel() {
    init {
        viewModelScope.launch(Dispatchers.IO) {
            repeat(100){
                delay(1000)
                val result = getUserInfoUserCase()
                Log.d("HomeViewModel", ": $result")
            }
        }
    }
}
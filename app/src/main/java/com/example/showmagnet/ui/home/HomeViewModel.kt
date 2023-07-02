package com.example.showmagnet.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.showmagnet.domain.model.MediaType
import com.example.showmagnet.domain.model.TimeWindow
import com.example.showmagnet.domain.use_case.show.GetAnimationUseCase
import com.example.showmagnet.domain.use_case.show.GetNowPlayingUseCase
import com.example.showmagnet.domain.use_case.show.GetPopularUseCase
import com.example.showmagnet.domain.use_case.show.GetTrendingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    getTrendingUseCase: GetTrendingUseCase,
    getNowPlayingUseCase: GetNowPlayingUseCase,
    getAnimationUseCase: GetAnimationUseCase,
    getPopularUseCase: GetPopularUseCase
) : ViewModel() {
    init {
        viewModelScope.launch(Dispatchers.IO) {
            var result = getTrendingUseCase(TimeWindow.DAY)
            Log.d("HomeViewModel", ": $result")
            result = getNowPlayingUseCase(MediaType.MOVIE)
            Log.d("HomeViewModel", ": $result")
            result = getAnimationUseCase(MediaType.MOVIE)
            Log.d("HomeViewModel", ": $result")
            result = getPopularUseCase(MediaType.MOVIE)
            Log.d("HomeViewModel", ": $result")
        }
    }
}
package com.example.showmagnet.ui.tv

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.showmagnet.domain.use_case.GetNetworkConnectivityStateUseCase
import com.example.showmagnet.domain.use_case.tv.GetTvCastUseCase
import com.example.showmagnet.domain.use_case.tv.GetTvDetailsUseCase
import com.example.showmagnet.domain.use_case.tv.GetTvImagesUseCase
import com.example.showmagnet.domain.use_case.tv.GetTvRecommendationsUseCase
import com.example.showmagnet.domain.use_case.tv.GetTvSeasonUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TvViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    val getNetworkConnectivityStateUseCase: GetNetworkConnectivityStateUseCase,
    val getTvDetailsUseCase: GetTvDetailsUseCase,
    val getTvCastUseCase: GetTvCastUseCase,
    val getTvImagesUseCase: GetTvImagesUseCase,
    val getTvSeasonUseCase: GetTvSeasonUseCase,
    val getTvRecommendationsUseCase: GetTvRecommendationsUseCase
) : ViewModel() {
    private val id: Int = checkNotNull(savedStateHandle["id"])

    init {
        viewModelScope.launch {
//            Log.d("TvViewModel", ": ${getTvDetailsUseCase(id)}")
//            Log.d("TvViewModel", ": ${getTvCastUseCase(id)}")
//            Log.d("TvViewModel", ": ${getTvImagesUseCase(id)}")
            Log.d("TvViewModel", ": ${getTvSeasonUseCase(id, 1)}")
//            Log.d("TvViewModel", ": ${getTvRecommendationsUseCase(id)}")
        }
    }
}
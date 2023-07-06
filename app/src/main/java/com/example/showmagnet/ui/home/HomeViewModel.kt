package com.example.showmagnet.ui.home

import androidx.lifecycle.viewModelScope
import com.example.showmagnet.domain.model.MediaType
import com.example.showmagnet.domain.model.TimeWindow
import com.example.showmagnet.domain.use_case.GetNetworkConnectivityStateUseCase
import com.example.showmagnet.domain.use_case.show.GetAnimationUseCase
import com.example.showmagnet.domain.use_case.show.GetPopularUseCase
import com.example.showmagnet.domain.use_case.show.GetTrendingUseCase
import com.example.showmagnet.domain.use_case.show.GetUpcomingUseCase
import com.example.showmagnet.ui.common.base.BaseViewModel
import com.example.showmagnet.ui.common.utils.NetworkStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    val getNetworkConnectivityStateUseCase: GetNetworkConnectivityStateUseCase,
    val getTrendingUseCase: GetTrendingUseCase,
    val getUpcomingUseCase: GetUpcomingUseCase,
    val getAnimationUseCase: GetAnimationUseCase,
    val getPopularUseCase: GetPopularUseCase,
) : BaseViewModel<HomeContract.Event, HomeContract.State, HomeContract.Effect>() {

    override fun setInitialState() = HomeContract.State()
    override fun handleEvents(event: HomeContract.Event) {
        when (event) {
            is HomeContract.Event.AnimeMediaTypeChange -> updateAnimeMediaType(event.mediaType)
            is HomeContract.Event.PopularMediaTypeChange -> updatePopularMediaType(event.mediaType)
            is HomeContract.Event.TrendingTimeWindowChange -> updateTrendingTimeWindow(event.timeWindow)
        }
    }


    private fun updateAnimeMediaType(mediaType: MediaType) = viewModelScope.launch {
        val result = getAnimationUseCase(mediaType)
        if (result.isSuccess) setState {
            copy(
                anime = result.getOrDefault(emptyList()),
                animeMediaType = mediaType
            )
        }
    }

    private fun updatePopularMediaType(mediaType: MediaType) = viewModelScope.launch {
        val result = getPopularUseCase(mediaType)
        if (result.isSuccess) setState {
            copy(
                popular = result.getOrDefault(emptyList()), popularMediaType = mediaType
            )
        }
    }

    private fun updateTrendingTimeWindow(timeWindow: TimeWindow) = viewModelScope.launch {
        val result = getTrendingUseCase(timeWindow)
        if (result.isSuccess) setState {
            copy(
                trending = result.getOrDefault(emptyList()), trendingTimeWindow = timeWindow
            )
        }
    }

    private fun updateUpcoming() = viewModelScope.launch {
        val result = getUpcomingUseCase()
        if (result.isSuccess) setState {
            copy(upcoming = result.getOrDefault(emptyList()))
        }
    }

    init {
        viewModelScope.launch {
            getNetworkConnectivityStateUseCase().collectLatest { state ->
                if (
                    state == NetworkStatus.Connected
                ) {
                    setState { copy(loading = true, connected = state) }
                    updateUpcoming()
                    updateTrendingTimeWindow(viewState.value.trendingTimeWindow)
                    updatePopularMediaType(viewState.value.popularMediaType)
                    updateAnimeMediaType(viewState.value.animeMediaType)
                    setState { copy(loading = false) }
                } else
                    setState { copy(connected = state) }

            }
        }
    }
}
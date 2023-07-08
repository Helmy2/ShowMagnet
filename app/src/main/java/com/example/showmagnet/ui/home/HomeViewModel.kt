package com.example.showmagnet.ui.home

import androidx.lifecycle.viewModelScope
import com.example.showmagnet.domain.model.NetworkUnavailableException
import com.example.showmagnet.domain.model.common.MediaType
import com.example.showmagnet.domain.model.common.TimeWindow
import com.example.showmagnet.domain.use_case.show.GetAnimationUseCase
import com.example.showmagnet.domain.use_case.show.GetPopularUseCase
import com.example.showmagnet.domain.use_case.show.GetTrendingUseCase
import com.example.showmagnet.domain.use_case.show.GetUpcomingUseCase
import com.example.showmagnet.ui.common.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
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
            HomeContract.Event.Refresh -> refresh()
        }
    }


    private fun updateAnimeMediaType(mediaType: MediaType) = viewModelScope.launch {
        val result = getAnimationUseCase(mediaType)
        if (result.isSuccess) setState {
            copy(
                anime = result.getOrDefault(emptyList()),
                animeMediaType = mediaType,
                connected = true,
                loading = false
            )
        } else {
            if (result.exceptionOrNull() is NetworkUnavailableException) setState {
                copy(connected = false)
            }
        }
    }

    private fun updatePopularMediaType(mediaType: MediaType) = viewModelScope.launch {
        val result = getPopularUseCase(mediaType)
        if (result.isSuccess) setState {
            copy(
                popular = result.getOrDefault(emptyList()),
                popularMediaType = mediaType,
                connected = true,
                loading = false
            )
        } else {
            if (result.exceptionOrNull() is NetworkUnavailableException) setState {
                copy(connected = false)
            }
        }
    }

    private fun updateTrendingTimeWindow(timeWindow: TimeWindow) = viewModelScope.launch {
        val result = getTrendingUseCase(timeWindow)
        if (result.isSuccess) setState {
            copy(
                trending = result.getOrDefault(emptyList()),
                trendingTimeWindow = timeWindow,
                connected = true,
                loading = false
            )
        } else {
            if (result.exceptionOrNull() is NetworkUnavailableException) setState {
                copy(connected = false)
            }
        }
    }

    private fun updateUpcoming() = viewModelScope.launch {
        val result = getUpcomingUseCase()
        if (result.isSuccess) setState {
            copy(
                upcoming = result.getOrDefault(emptyList()),
                connected = true,
                loading = false
            )
        } else {
            if (result.exceptionOrNull() is NetworkUnavailableException) setState {
                copy(connected = false)
            }
        }
    }

    private fun refresh() = viewModelScope.launch {
        setState { copy(loading = true) }

        updateUpcoming()
        updateTrendingTimeWindow(viewState.value.trendingTimeWindow)
        updatePopularMediaType(viewState.value.popularMediaType)
        updateAnimeMediaType(viewState.value.animeMediaType)

        delay(500)
        setState { copy(loading = false) }
    }


    init {
        refresh()
    }
}
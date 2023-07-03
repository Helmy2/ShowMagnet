package com.example.showmagnet.ui.home

import androidx.lifecycle.viewModelScope
import com.example.showmagnet.domain.model.MediaType
import com.example.showmagnet.domain.model.Show
import com.example.showmagnet.domain.model.TimeWindow
import com.example.showmagnet.domain.use_case.show.GetAnimationUseCase
import com.example.showmagnet.domain.use_case.show.GetPopularUseCase
import com.example.showmagnet.domain.use_case.show.GetTrendingUseCase
import com.example.showmagnet.domain.use_case.show.GetUpcomingUseCase
import com.example.showmagnet.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    val getTrendingUseCase: GetTrendingUseCase,
    val getUpcomingUseCase: GetUpcomingUseCase,
    val getAnimationUseCase: GetAnimationUseCase,
    val getPopularUseCase: GetPopularUseCase
) : BaseViewModel<HomeContract.Event, HomeContract.State, HomeContract.Effect>() {

    override fun setInitialState() = HomeContract.State()
    override fun handleEvents(event: HomeContract.Event) {
        when (event) {
            is HomeContract.Event.AnimeMediaTypeChange -> updateAnimeMediaType(event.mediaType)
            is HomeContract.Event.PopularMediaTypeChange -> updatePopularMediaType(event.mediaType)
            is HomeContract.Event.TrendingTimeWindowChange -> updateTrendingTimeWindow(event.timeWindow)
            is HomeContract.Event.Navigation.ToDigitalis -> handleNavigationToDigitalis(event.show)
        }
    }

    private fun handleNavigationToDigitalis(show: Show) {
        setEffect { HomeContract.Effect.Navigation.ToDigitalis(show) }
    }

    private fun updateAnimeMediaType(mediaType: MediaType) = viewModelScope.launch {
        val result = getAnimationUseCase(mediaType)
        if (result.isSuccess) setState {
            copy(
                anime = result.getOrDefault(emptyList()),
                animeMediaType = mediaType
            )
        }
        else setEffect {
            HomeContract.Effect.ShowErrorToast(
                result.exceptionOrNull()?.localizedMessage ?: ""
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
        else setEffect {
            HomeContract.Effect.ShowErrorToast(
                result.exceptionOrNull()?.localizedMessage ?: ""
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
        else setEffect {
            HomeContract.Effect.ShowErrorToast(
                result.exceptionOrNull()?.localizedMessage ?: ""
            )
        }
    }


    init {
        viewModelScope.launch(Dispatchers.Main) {
            setState { copy(loading = true) }
            var result = getUpcomingUseCase()
            if (result.isSuccess) setState { copy(upcoming = result.getOrDefault(emptyList())) }

            result = getTrendingUseCase(viewState.value.trendingTimeWindow)
            if (result.isSuccess) setState { copy(trending = result.getOrDefault(emptyList())) }

            result = getPopularUseCase(viewState.value.popularMediaType)
            if (result.isSuccess) setState { copy(popular = result.getOrDefault(emptyList())) }

            result = getAnimationUseCase(viewState.value.animeMediaType)
            if (result.isSuccess) setState { copy(anime = result.getOrDefault(emptyList())) }

            if (result.isFailure) setEffect {
                HomeContract.Effect.ShowErrorToast(
                    result.exceptionOrNull()?.localizedMessage ?: ""
                )
            }

            setState { copy(loading = false) }
        }
    }


}
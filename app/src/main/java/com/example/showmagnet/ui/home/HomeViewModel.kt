package com.example.showmagnet.ui.home

import androidx.lifecycle.viewModelScope
import com.example.showmagnet.domain.model.common.Category.ANIME
import com.example.showmagnet.domain.model.common.Category.POPULAR
import com.example.showmagnet.domain.model.common.Category.TRENDING
import com.example.showmagnet.domain.model.common.Category.UPCOMING
import com.example.showmagnet.domain.model.common.MediaType
import com.example.showmagnet.domain.model.common.NetworkUnavailableException
import com.example.showmagnet.domain.model.common.TimeWindow
import com.example.showmagnet.domain.use_case.person.GetTrendingPeopleUseCase
import com.example.showmagnet.domain.use_case.person.RefreshTrendingPeopleUseCase
import com.example.showmagnet.domain.use_case.show.GetCategoryUseCase
import com.example.showmagnet.domain.use_case.show.RefreshCategoryUseCase
import com.example.showmagnet.ui.common.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    val getTrendingPeopleUseCase: GetTrendingPeopleUseCase,
    val refreshTrendingPeopleUseCase: RefreshTrendingPeopleUseCase,
    val getCategoryUseCase: GetCategoryUseCase,
    val refreshCategoryUseCase: RefreshCategoryUseCase,
) : BaseViewModel<HomeContract.Event, HomeContract.State, HomeContract.Effect>() {

    private val errorHandler = CoroutineExceptionHandler { _, e ->
        if (e is NetworkUnavailableException) {
            setState { copy(loading = false, connected = false) }
        } else {
            setEffect { HomeContract.Effect.ShowErrorToast(e.localizedMessage.orEmpty()) }
        }
    }

    override fun setInitialState() = HomeContract.State()
    override fun handleEvents(event: HomeContract.Event) {
        when (event) {
            HomeContract.Event.Refresh -> refresh()

            is HomeContract.Event.PersonTimeWindowChange -> changePeopleTimeWidow(event.timeWindow)

            is HomeContract.Event.AnimeMediaTypeChange -> changeAnimeMedia(event.mediaType)
            is HomeContract.Event.PopularMediaTypeChange -> changePopularMedia(event.mediaType)
            is HomeContract.Event.TrendingMediaTypeChange -> changeTrendingMedia(event.mediaType)
            is HomeContract.Event.UpcomingMediaTypeChange -> changeUpcomingMedia(event.mediaType)
        }
    }

    private fun changeUpcomingMedia(type: MediaType) {
        viewModelScope.launch(errorHandler) {
            setState { copy(upcomingMediaType = type) }
            refreshCategoryUseCase(UPCOMING, type)
        }
    }

    private fun changePopularMedia(type: MediaType) {
        viewModelScope.launch(errorHandler) {
            setState { copy(popularMediaType = type) }
            refreshCategoryUseCase(POPULAR, type)
        }
    }

    private fun changeAnimeMedia(type: MediaType) {
        viewModelScope.launch(errorHandler) {
            setState { copy(animeMediaType = type) }
            refreshCategoryUseCase(ANIME, type)
        }
    }

    private fun changeTrendingMedia(type: MediaType) {
        viewModelScope.launch(errorHandler) {
            setState { copy(trendingMediaType = type) }
            refreshCategoryUseCase(TRENDING, type)
        }
    }

    private fun changePeopleTimeWidow(timeWindow: TimeWindow) {
        viewModelScope.launch(errorHandler) {
            setState { copy(personTimeWindow = timeWindow) }
            refreshTrendingPeopleUseCase(timeWindow)
        }
    }

    private fun refresh() = viewModelScope.launch(errorHandler) {
        setState { copy(loading = true, connected = true) }

        val state = viewState.value

        changeUpcomingMedia(state.upcomingMediaType)
        changePopularMedia(state.popularMediaType)
        changeAnimeMedia(state.animeMediaType)
        changeTrendingMedia(state.trendingMediaType)
        changePeopleTimeWidow(state.personTimeWindow)

        setState { copy(loading = false) }
    }

    init {
        val state = viewState.value
        viewModelScope.launch(errorHandler) {
            launch {
                getCategoryUseCase(UPCOMING, state.trendingMediaType).collectLatest {
                    setState { copy(upcoming = it) }
                }
            }
            launch {
                getCategoryUseCase(POPULAR, state.popularMediaType).collectLatest {
                    setState { copy(popular = it) }
                }
            }
            launch {
                getCategoryUseCase(ANIME, state.animeMediaType).collectLatest {
                    setState { copy(anime = it) }
                }
            }
            launch {
                getCategoryUseCase(TRENDING, state.trendingMediaType).collectLatest {
                    setState { copy(trending = it) }
                }
            }
            launch {
                getTrendingPeopleUseCase(state.personTimeWindow).collectLatest {
                    setState { copy(trendingPeople = it) }
                }
            }
        }
    }
}


package com.example.showmagnet.ui.home

import androidx.lifecycle.viewModelScope
import com.example.showmagnet.domain.model.common.Category
import com.example.showmagnet.domain.model.common.Category.ANIME
import com.example.showmagnet.domain.model.common.Category.POPULAR
import com.example.showmagnet.domain.model.common.Category.TRENDING
import com.example.showmagnet.domain.model.common.Category.UPCOMING
import com.example.showmagnet.domain.model.common.MediaType
import com.example.showmagnet.domain.model.common.Show
import com.example.showmagnet.domain.model.common.TimeWindow
import com.example.showmagnet.domain.use_case.person.GetTrendingPeopleUseCase
import com.example.showmagnet.domain.use_case.show.GetCategoryUseCase
import com.example.showmagnet.ui.common.BaseViewModel
import com.example.showmagnet.utils.collectResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    val getTrendingPeopleUseCase: GetTrendingPeopleUseCase,
    val getCategoryUseCase: GetCategoryUseCase,
) : BaseViewModel<HomeContract.Event, HomeContract.State, HomeContract.Effect>() {

    override fun setInitialState() = HomeContract.State()
    override fun handleEvents(event: HomeContract.Event) {
        when (event) {
            HomeContract.Event.Refresh -> refresh()

            is HomeContract.Event.PersonTimeWindowChange -> updatePopularPeopleTimeWidow(event.timeWindow)

            is HomeContract.Event.AnimeMediaTypeChange -> changeAnimeMedia(event.mediaType)
            is HomeContract.Event.PopularMediaTypeChange -> changePopularMedia(event.mediaType)
            is HomeContract.Event.TrendingMediaTypeChange -> changeTrendingMedia(event.mediaType)
            is HomeContract.Event.UpcomingMediaTypeChange -> changeUpcomingMedia(event.mediaType)
        }
    }

    private fun changeUpcomingMedia(type: MediaType) {
        updateCategory(UPCOMING, type) {
            setState {
                copy(upcomingMediaType = type, upcoming = it, connected = true, loading = false)
            }
        }
    }

    private fun changePopularMedia(type: MediaType) {
        updateCategory(POPULAR, type) {
            setState {
                copy(popularMediaType = type, popular = it, connected = true, loading = false)
            }
        }
    }

    private fun changeAnimeMedia(type: MediaType) {
        updateCategory(ANIME, type) {
            setState {
                copy(animeMediaType = type, anime = it, connected = true, loading = false)
            }
        }
    }

    private fun changeTrendingMedia(type: MediaType) {
        updateCategory(TRENDING, type) {
            setState {
                copy(trendingMediaType = type, trending = it, connected = true, loading = false)
            }
        }
    }

    private fun updatePopularPeopleTimeWidow(timeWindow: TimeWindow) {
        setState { copy(personTimeWindow = timeWindow) }
        updateTrendingPeople()
    }

    private fun updateTrendingPeople() {
        getTrendingPeopleUseCase(
            timeWindow = viewState.value.personTimeWindow
        ).collectResult(
            scope = viewModelScope,
            onSuccess = {
                setState { copy(trendingPeople = it, connected = true, loading = false) }
            },
            onNetworkFailure = {
                setState { copy(connected = false) }
            },
            onFailure = {
                setEffect { HomeContract.Effect.ShowErrorToast(it) }
            },
        )
    }

    private fun updateCategory(
        category: Category, mediaType: MediaType, onSuccess: (List<Show>) -> Unit
    ) {
        getCategoryUseCase(category, mediaType).collectResult(
            scope = viewModelScope,
            onSuccess = onSuccess,
            onNetworkFailure = {
                setState { copy(connected = false) }
            },
            onFailure = {
                setEffect { HomeContract.Effect.ShowErrorToast(it) }
            },
        )
    }

    private fun refresh() = viewModelScope.launch {
        setState { copy(loading = true) }

        updateTrendingPeople()

        updateCategory(
            UPCOMING,
            viewState.value.upcomingMediaType,
        ) {
            setState {
                copy(upcoming = it, connected = true, loading = false)
            }
        }

        updateCategory(
            TRENDING,
            viewState.value.trendingMediaType,
        ) {
            setState {
                copy(trending = it, connected = true, loading = false)
            }
        }

        updateCategory(
            POPULAR,
            viewState.value.popularMediaType,
        ) {
            setState {
                copy(popular = it, connected = true, loading = false)
            }
        }

        updateCategory(
            ANIME,
            viewState.value.animeMediaType,
        ) {
            setState {
                copy(anime = it, connected = true, loading = false)
            }
        }

        delay(500)
        setState { copy(loading = false) }
    }

    init {
        refresh()
    }
}


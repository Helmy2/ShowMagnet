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

    private var trendingShows: List<Show> = emptyList()
    private var animeShows: List<Show> = emptyList()
    private var popularShows: List<Show> = emptyList()
    private var upcomingShows: List<Show> = emptyList()

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
        val result = upcomingShows.filter { it.type == type }
        if (result.isEmpty()) {
            updateCategory(UPCOMING, type) {
                upcomingShows = it
            }
        }
        setState {
            copy(
                upcomingMediaType = type,
                upcoming = result,
                connected = true,
                loading = false
            )
        }
    }

    private fun changePopularMedia(type: MediaType) {
        val result = popularShows.filter { it.type == type }
        if (result.isEmpty()) {
            updateCategory(POPULAR, type) {
                popularShows = it
            }
        }
        setState {
            copy(
                popularMediaType = type,
                popular = result,
                connected = true,
                loading = false
            )
        }
    }

    private fun changeAnimeMedia(type: MediaType) {
        val result = animeShows.filter { it.type == type }
        if (result.isEmpty()) {
            updateCategory(ANIME, type) {
                animeShows = it
            }
        }
        setState {
            copy(
                animeMediaType = type,
                anime = result,
                connected = true,
                loading = false
            )
        }
    }

    private fun changeTrendingMedia(type: MediaType) {
        val result = trendingShows.filter { it.type == type }
        if (result.isEmpty()) {
            updateCategory(TRENDING, type) {
                trendingShows = it
            }
        }
        setState {
            copy(
                trendingMediaType = type,
                trending = result,
                connected = true,
                loading = false
            )
        }
    }

    private fun updatePopularPeopleTimeWidow(timeWindow: TimeWindow) {
        setState { copy(personTimeWindow = timeWindow) }
        updateTrendingPeople()
    }

    private fun updateTrendingPeople() {
        getTrendingPeopleUseCase(
            timeWindow = viewState.value.personTimeWindow
        ).collectResult(scope = viewModelScope, onSuccess = { list ->
            val result = list.filter {
                it.timeWindow == viewState.value.personTimeWindow
            }
            setState { copy(trendingPeople = result, connected = true, loading = false) }
        }, onNetworkFailure = {
            setState { copy(connected = false) }
        }, onFailure = {
            setEffect { HomeContract.Effect.ShowErrorToast(it) }
        })
    }

    private fun updateCategory(
        category: Category,
        mediaType: MediaType,
        onSuccess: (List<Show>) -> Unit
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
            upcomingShows = it
            changeUpcomingMedia(viewState.value.upcomingMediaType)
        }

        updateCategory(
            TRENDING,
            viewState.value.trendingMediaType,
        ) {
            trendingShows = it
            changeTrendingMedia(viewState.value.trendingMediaType)
        }

        updateCategory(
            POPULAR,
            viewState.value.popularMediaType,
        ) {
            popularShows = it
            changePopularMedia(viewState.value.popularMediaType)
        }

        updateCategory(
            ANIME,
            viewState.value.animeMediaType,
        ) {
            animeShows = it
            changeAnimeMedia(viewState.value.animeMediaType)
        }

        delay(500)
        setState { copy(loading = false) }
    }

    init {
        refresh()
    }
}


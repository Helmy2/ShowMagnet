package com.example.showmagnet.ui.home

import com.example.showmagnet.domain.model.MediaType
import com.example.showmagnet.domain.model.Show
import com.example.showmagnet.domain.model.TimeWindow
import com.example.showmagnet.ui.base.ViewEffect
import com.example.showmagnet.ui.base.ViewEvent
import com.example.showmagnet.ui.base.ViewState
import com.example.showmagnet.ui.utils.NetworkStatus

class HomeContract {
    sealed class Event : ViewEvent {
        class TrendingTimeWindowChange(val timeWindow: TimeWindow) : Event()
        class PopularMediaTypeChange(val mediaType: MediaType) : Event()
        class AnimeMediaTypeChange(val mediaType: MediaType) : Event()

        sealed class Navigation : Event() {
            class ToDigitalis(val show: Show) : Navigation()
        }
    }


    data class State(
        val loading: Boolean = true,
        val connected: NetworkStatus = NetworkStatus.Unknown,
        val trending: List<Show> = emptyList(),
        val popular: List<Show> = emptyList(),
        val upcoming: List<Show> = emptyList(),
        val anime: List<Show> = emptyList(),
        val trendingTimeWindow: TimeWindow = TimeWindow.DAY,
        val popularMediaType: MediaType = MediaType.MOVIE,
        val animeMediaType: MediaType = MediaType.MOVIE,
    ) : ViewState

    sealed class Effect : ViewEffect {
        class ShowErrorToast(val message: String) : Effect()

        sealed class Navigation : Effect() {
            class ToDigitalis(val show: Show) : Navigation()
        }
    }
}
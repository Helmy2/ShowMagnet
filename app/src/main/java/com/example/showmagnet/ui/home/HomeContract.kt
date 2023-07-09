package com.example.showmagnet.ui.home

import com.example.showmagnet.domain.model.common.MediaType
import com.example.showmagnet.domain.model.common.Show
import com.example.showmagnet.domain.model.common.TimeWindow
import com.example.showmagnet.domain.model.person.Person
import com.example.showmagnet.ui.common.ViewEffect
import com.example.showmagnet.ui.common.ViewEvent
import com.example.showmagnet.ui.common.ViewState

class HomeContract {
    sealed class Event : ViewEvent {
        class TrendingShowTimeWindowChange(val timeWindow: TimeWindow) : Event()
        class TrendingPersonTimeWindowChange(val timeWindow: TimeWindow) : Event()
        class PopularMediaTypeChange(val mediaType: MediaType) : Event()
        class AnimeMediaTypeChange(val mediaType: MediaType) : Event()
        object Refresh : Event()

    }


    data class State(
        val loading: Boolean = false,
        val connected: Boolean = false,
        val trendingShow: List<Show> = emptyList(),
        val popular: List<Show> = emptyList(),
        val upcoming: List<Show> = emptyList(),
        val anime: List<Show> = emptyList(),
        val tradingPeople: List<Person> = emptyList(),
        val trendingShowTimeWindow: TimeWindow = TimeWindow.DAY,
        val trendingPersonTimeWindow: TimeWindow = TimeWindow.DAY,
        val popularMediaType: MediaType = MediaType.MOVIE,
        val animeMediaType: MediaType = MediaType.MOVIE,
    ) : ViewState

    sealed class Effect : ViewEffect {
        class ShowErrorToast(val message: String) : Effect()
    }

    sealed class Navigation {
        class ToMovie(val id: Int) : Navigation()
        class ToTv(val id: Int) : Navigation()
        class ToPerson(val id: Int) : Navigation()
    }
}
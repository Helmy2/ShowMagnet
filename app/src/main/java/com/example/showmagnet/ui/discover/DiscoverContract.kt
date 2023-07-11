package com.example.showmagnet.ui.discover

import com.example.showmagnet.domain.model.common.Genre
import com.example.showmagnet.domain.model.common.MediaType
import com.example.showmagnet.domain.model.common.Show
import com.example.showmagnet.domain.model.common.SortBy
import com.example.showmagnet.ui.common.ViewEffect
import com.example.showmagnet.ui.common.ViewEvent
import com.example.showmagnet.ui.common.ViewState

class DiscoverContract {
    sealed class Event : ViewEvent {
        object Refresh : Event()

        object LoadMore : Event()
        class MediaTypeChange(
            val mediaType: MediaType,
            val genre: Genre?,
            val adult: Boolean,
            val sortBy: SortBy,
        ) : Event()

    }

    data class State(
        val loading: Boolean = true,
        val connected: Boolean = true,
        val shows: List<Show> = emptyList(),
        val mediaType: MediaType = MediaType.MOVIE,
        val genre: Genre? = null,
        val adult: Boolean = false,
        val sortBy: SortBy = SortBy.Popularity
    ) : ViewState

    sealed class Navigation {
        class ToMovie(val id: Int) : Navigation()
        class ToTv(val id: Int) : Navigation()
    }

    sealed class Effect : ViewEffect {
        class ShowErrorToast(val message: String) : Effect()
    }
}
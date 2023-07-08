package com.example.showmagnet.ui.movie

import com.example.showmagnet.domain.model.common.Cast
import com.example.showmagnet.domain.model.common.Image
import com.example.showmagnet.domain.model.common.Show
import com.example.showmagnet.domain.model.movie.Movie
import com.example.showmagnet.ui.common.ViewEffect
import com.example.showmagnet.ui.common.ViewEvent
import com.example.showmagnet.ui.common.ViewState

class MovieContract {
    sealed class Event : ViewEvent {
        object Refresh : Event()

    }

    data class State(
        val loading: Boolean = true,
        val connected: Boolean = false,
        val movie: Movie? = null,
        val castList: List<Cast>? = null,
        val collection: List<Show>? = null,
        val recommendations: List<Show>? = null,
        val imageList: List<Image>? = null,
    ) : ViewState

    sealed class Effect : ViewEffect {
        class ShowErrorToast(val message: String) : Effect()
    }

    sealed class Navigation {
        class ToDigitalis(val show: Show) : Navigation()
        class ToPerson(val id: Int) : Navigation()
    }
}
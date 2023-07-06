package com.example.showmagnet.ui.movie

import com.example.showmagnet.domain.model.Cast
import com.example.showmagnet.domain.model.Image
import com.example.showmagnet.domain.model.Movie
import com.example.showmagnet.domain.model.Show
import com.example.showmagnet.ui.common.base.ViewEffect
import com.example.showmagnet.ui.common.base.ViewEvent
import com.example.showmagnet.ui.common.base.ViewState
import com.example.showmagnet.ui.common.utils.NetworkStatus

class MovieContract {
    sealed class Event : ViewEvent {

    }

    data class State(
        val loading: Boolean = true,
        val connected: NetworkStatus = NetworkStatus.Unknown,
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
package com.example.showmagnet.ui.tv

import com.example.showmagnet.domain.model.common.Cast
import com.example.showmagnet.domain.model.common.Image
import com.example.showmagnet.domain.model.common.Show
import com.example.showmagnet.domain.model.tv.Episode
import com.example.showmagnet.domain.model.tv.Tv
import com.example.showmagnet.ui.common.ViewEffect
import com.example.showmagnet.ui.common.ViewEvent
import com.example.showmagnet.ui.common.ViewState

class TvContract {
    sealed class Event : ViewEvent {
        data class ChangeNumberOfSeasons(val number: Int) : Event()
        object Refresh : Event()

    }

    data class State(
        val loading: Boolean = true,
        val connected: Boolean = false,
        val tv: Tv? = null,
        val episodeList: List<Episode>? = null,
        val castList: List<Cast>? = null,
        val recommendations: List<Show>? = null,
        val imageList: List<Image>? = null,
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
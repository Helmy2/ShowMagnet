package com.example.showmagnet.ui.tv

import com.example.showmagnet.domain.model.Cast
import com.example.showmagnet.domain.model.Episode
import com.example.showmagnet.domain.model.Image
import com.example.showmagnet.domain.model.Show
import com.example.showmagnet.domain.model.Tv
import com.example.showmagnet.ui.common.base.ViewEffect
import com.example.showmagnet.ui.common.base.ViewEvent
import com.example.showmagnet.ui.common.base.ViewState
import com.example.showmagnet.ui.common.utils.NetworkStatus

class TvContract {
    sealed class Event : ViewEvent {
        data class ChangeNumberOfSeasons(val number: Int) : Event()
    }

    data class State(
        val loading: Boolean = true,
        val connected: NetworkStatus = NetworkStatus.Unknown,
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
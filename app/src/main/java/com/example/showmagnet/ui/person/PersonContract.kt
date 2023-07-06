package com.example.showmagnet.ui.person

import com.example.showmagnet.domain.model.Image
import com.example.showmagnet.domain.model.Show
import com.example.showmagnet.domain.model.person.PersonDetails
import com.example.showmagnet.ui.common.base.ViewEffect
import com.example.showmagnet.ui.common.base.ViewEvent
import com.example.showmagnet.ui.common.base.ViewState
import com.example.showmagnet.ui.common.utils.NetworkStatus

class PersonContract {
    sealed class Event : ViewEvent {

    }

    data class State(
        val loading: Boolean = true,
        val connected: NetworkStatus = NetworkStatus.Unknown,
        val person: PersonDetails? = null,
        val movieCredits: List<Show>? = null,
        val tvCredits: List<Show>? = null,
        val imageList: List<Image>? = null,
    ) : ViewState

    sealed class Effect : ViewEffect {
        class ShowErrorToast(val message: String) : Effect()
    }

    sealed class Navigation {
        class ToMovie(val id: Int) : Navigation()
        class ToTv(val id: Int) : Navigation()
    }
}
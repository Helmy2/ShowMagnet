package com.example.showmagnet.ui.person

import com.example.showmagnet.domain.model.common.Image
import com.example.showmagnet.domain.model.common.Show
import com.example.showmagnet.domain.model.person.PersonDetails
import com.example.showmagnet.ui.common.ViewEffect
import com.example.showmagnet.ui.common.ViewEvent
import com.example.showmagnet.ui.common.ViewState

class PersonContract {
    sealed class Event : ViewEvent {
        object Refresh : Event()
        object ToggleFavorite : Event()
    }

    data class State(
        val loading: Boolean = true,
        val connected: Boolean = false,
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
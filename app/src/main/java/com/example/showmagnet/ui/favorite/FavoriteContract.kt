package com.example.showmagnet.ui.favorite

import com.example.showmagnet.domain.model.common.Show
import com.example.showmagnet.domain.model.person.Person
import com.example.showmagnet.ui.common.ViewEffect
import com.example.showmagnet.ui.common.ViewEvent
import com.example.showmagnet.ui.common.ViewState

class FavoriteContract {
    sealed class Event : ViewEvent {
        object Refresh : Event()

    }

    data class State(
        val loading: Boolean = true,
        val connected: Boolean = true,
        val movie: List<Show> = emptyList(),
        val tv: List<Show> = emptyList(),
        val people: List<Person> = emptyList(),
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
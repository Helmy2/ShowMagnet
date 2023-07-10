package com.example.showmagnet.ui.favorite

import androidx.lifecycle.viewModelScope
import com.example.showmagnet.domain.model.NetworkUnavailableException
import com.example.showmagnet.domain.use_case.favorite.GetFavoriteMovieUseCase
import com.example.showmagnet.domain.use_case.favorite.GetFavoritePeopleUseCase
import com.example.showmagnet.domain.use_case.favorite.GetFavoriteTvUseCase
import com.example.showmagnet.ui.common.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val getFavoriteMovieUseCase: GetFavoriteMovieUseCase,
    private val getFavoriteTvUseCase: GetFavoriteTvUseCase,
    private val getFavoritePeopleUseCase: GetFavoritePeopleUseCase
) : BaseViewModel<FavoriteContract.Event, FavoriteContract.State, FavoriteContract.Effect>() {


    override fun setInitialState() = FavoriteContract.State()
    override fun handleEvents(event: FavoriteContract.Event) {
        when (event) {
            FavoriteContract.Event.Refresh -> refresh()
        }
    }

    private suspend fun updateFavoriteMovie() {
        val result = getFavoriteMovieUseCase()
        if (result.isSuccess) setState {
            copy(
                movie = result.getOrDefault(emptyList()),
                connected = true,
            )
        } else {
            if (result.exceptionOrNull() is NetworkUnavailableException) setState {
                copy(connected = false)
            }
        }
    }

    private suspend fun updateFavoriteTv() {
        val result = getFavoriteTvUseCase()
        if (result.isSuccess) setState {
            copy(
                tv = result.getOrDefault(emptyList()),
                connected = true,
            )
        } else {
            if (result.exceptionOrNull() is NetworkUnavailableException) setState {
                copy(connected = false)
            }
        }
    }

    private suspend fun updateFavoritePeople() {
        val result = getFavoritePeopleUseCase()
        if (result.isSuccess) setState {
            copy(
                people = result.getOrDefault(emptyList()),
                connected = true,
            )
        } else {
            if (result.exceptionOrNull() is NetworkUnavailableException) setState {
                copy(connected = false)
            }
        }
    }

    private fun refresh() = viewModelScope.launch {
        setState { copy(loading = true) }

        updateFavoriteMovie()
        updateFavoriteTv()
        updateFavoritePeople()

        setState { copy(loading = false) }
    }


    init {
        refresh()
    }
}
package com.example.showmagnet.ui.favorite

import androidx.lifecycle.viewModelScope
import com.example.showmagnet.domain.use_case.favorite.GetFavoriteMovieUseCase
import com.example.showmagnet.domain.use_case.favorite.GetFavoritePeopleUseCase
import com.example.showmagnet.domain.use_case.favorite.GetFavoriteTvUseCase
import com.example.showmagnet.ui.common.BaseViewModel
import com.example.showmagnet.utils.collectResult
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
        getFavoriteMovieUseCase().collectResult(
            viewModelScope,
            onSuccess = {
                setState { copy(movie = it, connected = true) }
            },
            onNetworkFailure = {
                setState { copy(connected = false, loading = false) }
            },
            onFailure = { setEffect { FavoriteContract.Effect.ShowErrorToast(it) } },
        )
    }

    private suspend fun updateFavoriteTv() {
        getFavoriteTvUseCase().collectResult(
            viewModelScope,
            onSuccess = {
                setState { copy(tv = it, connected = true) }
            },
            onNetworkFailure = {
                setState { copy(connected = false, loading = false) }
            },
            onFailure = { setEffect { FavoriteContract.Effect.ShowErrorToast(it) } },
        )
    }

    private suspend fun updateFavoritePeople() {
       getFavoritePeopleUseCase().collectResult(
            viewModelScope,
            onSuccess = {
                setState { copy(people = it, connected = true) }
            },
            onNetworkFailure = {
                setState { copy(connected = false, loading = false) }
            },
            onFailure = { setEffect { FavoriteContract.Effect.ShowErrorToast(it) } },
        )
    }

    private fun refresh() = viewModelScope.launch {
        setState { copy(loading = true) }

        updateFavoriteMovie()
        updateFavoriteTv()
        updateFavoritePeople()

        setState { copy(loading = false) }
    }

}
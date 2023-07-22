package com.example.showmagnet.ui.favorite

import androidx.lifecycle.viewModelScope
import com.example.showmagnet.domain.model.common.NetworkUnavailableException
import com.example.showmagnet.domain.use_case.favorite.GetFavoriteMovieUseCase
import com.example.showmagnet.domain.use_case.favorite.GetFavoritePeopleUseCase
import com.example.showmagnet.domain.use_case.favorite.GetFavoriteTvUseCase
import com.example.showmagnet.domain.use_case.favorite.RefreshFavoriteMovieUseCase
import com.example.showmagnet.domain.use_case.favorite.RefreshFavoritePeopleUseCase
import com.example.showmagnet.domain.use_case.favorite.RefreshFavoriteTvUseCase
import com.example.showmagnet.ui.common.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val getFavoriteMovieUseCase: GetFavoriteMovieUseCase,
    private val getFavoriteTvUseCase: GetFavoriteTvUseCase,
    private val getFavoritePeopleUseCase: GetFavoritePeopleUseCase,
    private val refreshFavoriteMovieUseCase: RefreshFavoriteMovieUseCase,
    private val refreshFavoriteTvUseCase: RefreshFavoriteTvUseCase,
    private val refreshFavoritePeopleUseCase: RefreshFavoritePeopleUseCase
) : BaseViewModel<FavoriteContract.Event, FavoriteContract.State, FavoriteContract.Effect>() {

    private val errorHandler = CoroutineExceptionHandler { _, e ->
        if (e is NetworkUnavailableException) {
            setState { copy(connected = false) }
        } else {
            setEffect { FavoriteContract.Effect.ShowErrorToast(e.localizedMessage.orEmpty()) }
        }
    }

    override fun setInitialState() = FavoriteContract.State()
    override fun handleEvents(event: FavoriteContract.Event) {
        when (event) {
            FavoriteContract.Event.Refresh -> refresh()
        }
    }

    private suspend fun changeFavoriteMovie() {
        viewModelScope.launch(errorHandler) {
            refreshFavoriteMovieUseCase()
        }
    }

    private suspend fun changeFavoriteTv() {
        viewModelScope.launch(errorHandler) {
            refreshFavoriteTvUseCase()
        }
    }

    private suspend fun changeFavoritePeople() {
        viewModelScope.launch(errorHandler) {
            refreshFavoritePeopleUseCase()
        }
    }

    private fun refresh() = viewModelScope.launch {
        setState { copy(loading = true, connected = true) }

        changeFavoriteMovie()
        changeFavoriteTv()
        changeFavoritePeople()

        setState { copy(loading = false) }
    }

    init {
        viewModelScope.launch(errorHandler) {
            launch {
                getFavoriteMovieUseCase().collectLatest {
                    setState { copy(movie = it) }
                }
            }
            launch {
                getFavoriteTvUseCase().collectLatest {
                    setState { copy(tv = it) }
                }
            }
            launch {
                getFavoritePeopleUseCase().collectLatest {
                    setState { copy(people = it) }
                }
            }
        }
        refresh()
    }

}
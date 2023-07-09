package com.example.showmagnet.ui.movie

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.showmagnet.domain.model.NetworkUnavailableException
import com.example.showmagnet.domain.use_case.movie.AddMovieToFavoriteUseCase
import com.example.showmagnet.domain.use_case.movie.GetCastUseCase
import com.example.showmagnet.domain.use_case.movie.GetCollectionUseCase
import com.example.showmagnet.domain.use_case.movie.GetImagesUseCase
import com.example.showmagnet.domain.use_case.movie.GetMovieDetailsUseCase
import com.example.showmagnet.domain.use_case.movie.GetRecommendationsUseCase
import com.example.showmagnet.domain.use_case.movie.RemoveMovieFromFavoriteUseCase
import com.example.showmagnet.ui.common.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    val getMovieDetailsUseCase: GetMovieDetailsUseCase,
    val getCastUseCase: GetCastUseCase,
    val getCollectionUseCase: GetCollectionUseCase,
    val getImagesUseCase: GetImagesUseCase,
    val getRecommendationsUseCase: GetRecommendationsUseCase,
    val addMovieToFavoriteUseCase: AddMovieToFavoriteUseCase,
    val removeMovieFromFavoriteUseCase: RemoveMovieFromFavoriteUseCase
) : BaseViewModel<MovieContract.Event, MovieContract.State, MovieContract.Effect>() {
    private val id: Int = checkNotNull(savedStateHandle["id"])

    override fun setInitialState() = MovieContract.State()
    override fun handleEvents(event: MovieContract.Event) {
        when (event) {
            MovieContract.Event.Refresh -> refresh()
            MovieContract.Event.ToggleFavorite -> toggleFavorite()
        }
    }

    private fun toggleFavorite() = viewModelScope.launch {
        viewState.value.movie?.favorite?.let {
            setState { copy(movie = viewState.value.movie?.copy(favorite = !it)) }
            if (!it) {
                addMovieToFavoriteUseCase(id)
            } else {
                removeMovieFromFavoriteUseCase(id)
            }
            updateMovieDetails()
        }
    }


    init {
        refresh()
    }

    private suspend fun updateMovieDetails() {
        val movieResult = getMovieDetailsUseCase(id)
        if (movieResult.isFailure) {
            if (movieResult.exceptionOrNull() is NetworkUnavailableException) setState {
                copy(loading = false, connected = false)
            }
            else {
                setEffect {
                    MovieContract.Effect.ShowErrorToast(
                        movieResult.exceptionOrNull()?.localizedMessage ?: ""
                    )
                }
            }
            return
        }

        setState {
            copy(
                movie = movieResult.getOrNull(), connected = true, loading = false
            )
        }
    }

    private fun refresh() = viewModelScope.launch {
        setState { copy(loading = true) }

        updateMovieDetails()

        val cast = getCastUseCase(id)
        setState {
            copy(
                castList = cast.getOrNull(), connected = true, loading = false
            )
        }

        viewState.value.movie?.collectionId?.let {
            val collection = getCollectionUseCase(it)
            setState {
                copy(
                    collection = collection.getOrNull(), connected = true, loading = false
                )
            }
        }

        val images = getImagesUseCase(id)
        setState {
            copy(
                imageList = images.getOrNull(), connected = true, loading = false
            )
        }

        val recommendations = getRecommendationsUseCase(id)
        setState {
            copy(
                recommendations = recommendations.getOrNull(), connected = true, loading = false
            )
        }

        delay(500)
        setState { copy(loading = false) }
    }


}
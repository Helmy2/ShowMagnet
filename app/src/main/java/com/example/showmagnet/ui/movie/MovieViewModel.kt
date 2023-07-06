package com.example.showmagnet.ui.movie

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.showmagnet.domain.use_case.GetNetworkConnectivityStateUseCase
import com.example.showmagnet.domain.use_case.movie.GetCastUseCase
import com.example.showmagnet.domain.use_case.movie.GetCollectionUseCase
import com.example.showmagnet.domain.use_case.movie.GetImagesUseCase
import com.example.showmagnet.domain.use_case.movie.GetMovieDetailsUseCase
import com.example.showmagnet.domain.use_case.movie.GetRecommendationsUseCase
import com.example.showmagnet.ui.common.base.BaseViewModel
import com.example.showmagnet.ui.common.utils.NetworkStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    val getNetworkConnectivityStateUseCase: GetNetworkConnectivityStateUseCase,
    val getMovieDetailsUseCase: GetMovieDetailsUseCase,
    val getCastUseCase: GetCastUseCase,
    val getCollectionUseCase: GetCollectionUseCase,
    val getImagesUseCase: GetImagesUseCase,
    val getRecommendationsUseCase: GetRecommendationsUseCase
) : BaseViewModel<MovieContract.Event, MovieContract.State, MovieContract.Effect>() {
    private val id: Int = checkNotNull(savedStateHandle["id"])

    override fun setInitialState() = MovieContract.State()
    override fun handleEvents(event: MovieContract.Event) {

    }

    init {
        viewModelScope.launch {
            getNetworkConnectivityStateUseCase().collectLatest { state ->
                if (
                    state == NetworkStatus.Connected
                ) {
                    setState { copy(loading = true, connected = state) }
                    updateMovieDetails()
                    setState { copy(loading = false) }
                } else
                    setState { copy(connected = state) }
            }
        }
    }

    private fun updateMovieDetails() = viewModelScope.launch {
        val movieResult = getMovieDetailsUseCase(id)
        if (movieResult.isFailure) {
            setEffect {
                MovieContract.Effect.ShowErrorToast(
                    movieResult.exceptionOrNull()?.localizedMessage ?: ""
                )
            }
            return@launch
        }
        setState { copy(movie = movieResult.getOrNull()) }

        val cast = getCastUseCase(id)
        setState { copy(castList = cast.getOrNull()) }

        movieResult.getOrNull()?.collectionId?.let {
            val collection = getCollectionUseCase(it)
            setState { copy(collection = collection.getOrNull()) }
        }

        val images = getImagesUseCase(id)
        setState { copy(imageList = images.getOrNull()) }

        val recommendations = getRecommendationsUseCase(id)
        setState { copy(recommendations = recommendations.getOrNull()) }
    }


}
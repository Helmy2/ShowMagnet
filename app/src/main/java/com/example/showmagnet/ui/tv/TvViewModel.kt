package com.example.showmagnet.ui.tv

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.showmagnet.domain.model.common.NetworkUnavailableException
import com.example.showmagnet.domain.use_case.tv.AddTvToFavoriteUseCase
import com.example.showmagnet.domain.use_case.tv.GetTvCastUseCase
import com.example.showmagnet.domain.use_case.tv.GetTvDetailsUseCase
import com.example.showmagnet.domain.use_case.tv.GetTvImagesUseCase
import com.example.showmagnet.domain.use_case.tv.GetTvRecommendationsUseCase
import com.example.showmagnet.domain.use_case.tv.GetTvSeasonUseCase
import com.example.showmagnet.domain.use_case.tv.RemoveTvFromFavoriteUseCase
import com.example.showmagnet.ui.common.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TvViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    val getTvDetailsUseCase: GetTvDetailsUseCase,
    val getTvCastUseCase: GetTvCastUseCase,
    val getTvImagesUseCase: GetTvImagesUseCase,
    val getTvSeasonUseCase: GetTvSeasonUseCase,
    val getTvRecommendationsUseCase: GetTvRecommendationsUseCase,
    val addTvToFavoriteUseCase: AddTvToFavoriteUseCase,
    val removeTvFromFavoriteUseCase: RemoveTvFromFavoriteUseCase
) : BaseViewModel<TvContract.Event, TvContract.State, TvContract.Effect>() {
    private val id: Int = checkNotNull(savedStateHandle["id"])

    override fun setInitialState() = TvContract.State()

    override fun handleEvents(event: TvContract.Event) {
        when (event) {
            is TvContract.Event.ChangeNumberOfSeasons -> updateSeasons(event.number)
            TvContract.Event.Refresh -> refresh()
            TvContract.Event.ToggleFavorite -> toggleFavorite()
        }
    }

    private fun toggleFavorite() = viewModelScope.launch {
        viewState.value.tv?.favorite?.let {
            setState { copy(tv = viewState.value.tv?.copy(favorite = !it)) }
            if (!it) {
                addTvToFavoriteUseCase(id)
            } else {
                removeTvFromFavoriteUseCase(id)
            }
            updateTvDetails()
        }
    }

    private suspend fun updateTvDetails() {
        val tvResult = getTvDetailsUseCase(id)

        if (tvResult.isFailure) {
            if (tvResult.exceptionOrNull() is NetworkUnavailableException) setState {
                copy(loading = false, connected = false)
            }
            else {
                setEffect {
                    TvContract.Effect.ShowErrorToast(
                        tvResult.exceptionOrNull()?.localizedMessage ?: ""
                    )
                }
            }
            return
        }

        setState {
            copy(
                tv = tvResult.getOrNull(), loading = false, connected = true,
            )
        }
    }

    private fun updateSeasons(number: Int) = viewModelScope.launch {
        val seasons = getTvSeasonUseCase(id, number)
        setState { copy(episodeList = seasons.getOrNull()) }
    }

    init {
        refresh()
    }

    private fun refresh() = viewModelScope.launch {
        setState { copy(loading = true) }


        updateTvDetails()

        val cast = getTvCastUseCase(id)
        setState { copy(castList = cast.getOrNull()) }

        val seasons = getTvSeasonUseCase(id, 1)
        setState {
            copy(
                episodeList = seasons.getOrNull(), connected = true,
                loading = false
            )
        }

        val images = getTvImagesUseCase(id)
        setState {
            copy(
                imageList = images.getOrNull(), connected = true,
                loading = false
            )
        }

        val recommendations = getTvRecommendationsUseCase(id)
        setState {
            copy(
                recommendations = recommendations.getOrNull(), connected = true,
                loading = false
            )
        }

        delay(500)
        setState { copy(loading = false) }
    }


}
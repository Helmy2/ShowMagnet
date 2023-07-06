package com.example.showmagnet.ui.tv

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.showmagnet.domain.use_case.GetNetworkConnectivityStateUseCase
import com.example.showmagnet.domain.use_case.tv.GetTvCastUseCase
import com.example.showmagnet.domain.use_case.tv.GetTvDetailsUseCase
import com.example.showmagnet.domain.use_case.tv.GetTvImagesUseCase
import com.example.showmagnet.domain.use_case.tv.GetTvRecommendationsUseCase
import com.example.showmagnet.domain.use_case.tv.GetTvSeasonUseCase
import com.example.showmagnet.ui.common.base.BaseViewModel
import com.example.showmagnet.ui.common.utils.NetworkStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TvViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    val getNetworkConnectivityStateUseCase: GetNetworkConnectivityStateUseCase,
    val getTvDetailsUseCase: GetTvDetailsUseCase,
    val getTvCastUseCase: GetTvCastUseCase,
    val getTvImagesUseCase: GetTvImagesUseCase,
    val getTvSeasonUseCase: GetTvSeasonUseCase,
    val getTvRecommendationsUseCase: GetTvRecommendationsUseCase
) : BaseViewModel<TvContract.Event, TvContract.State, TvContract.Effect>() {
    private val id: Int = checkNotNull(savedStateHandle["id"])

    override fun setInitialState() = TvContract.State()

    override fun handleEvents(event: TvContract.Event) {
        when (event) {
            is TvContract.Event.ChangeNumberOfSeasons -> updateSeasons(event.number)
        }
    }

    private fun updateSeasons(number: Int) = viewModelScope.launch {
        val seasons = getTvSeasonUseCase(id, number)
        setState { copy(episodeList = seasons.getOrNull()) }
    }

    init {
        viewModelScope.launch {
            getNetworkConnectivityStateUseCase().collectLatest { state ->
                if (
                    state == NetworkStatus.Connected
                ) {
                    setState { copy(loading = true, connected = state) }
                    updateTvDetails()
                    setState { copy(loading = false) }
                } else
                    setState { copy(connected = state) }
            }
        }
    }

    private suspend fun updateTvDetails() {
        val tvResult = getTvDetailsUseCase(id)

        setState { copy(tv = tvResult.getOrNull()) }

        val cast = getTvCastUseCase(id)
        setState { copy(castList = cast.getOrNull()) }

        val seasons = getTvSeasonUseCase(id, 1)
        setState { copy(episodeList = seasons.getOrNull()) }

        val images = getTvImagesUseCase(id)
        setState { copy(imageList = images.getOrNull()) }

        val recommendations = getTvRecommendationsUseCase(id)
        setState { copy(recommendations = recommendations.getOrNull()) }

    }


}
package com.example.showmagnet.ui.person

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.showmagnet.domain.use_case.GetNetworkConnectivityStateUseCase
import com.example.showmagnet.domain.use_case.person.GetPersonDetailsUseCase
import com.example.showmagnet.domain.use_case.person.GetPersonImagesUseCase
import com.example.showmagnet.domain.use_case.person.GetPersonMovieCreditsUseCase
import com.example.showmagnet.domain.use_case.person.GetPersonTvCreditsUseCase
import com.example.showmagnet.ui.common.base.BaseViewModel
import com.example.showmagnet.ui.common.utils.NetworkStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PersonViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    val getNetworkConnectivityStateUseCase: GetNetworkConnectivityStateUseCase,
    val getPersonImagesUseCase: GetPersonImagesUseCase,
    val getPersonDetailsUseCase: GetPersonDetailsUseCase,
    val getPersonMovieCreditsUseCase: GetPersonMovieCreditsUseCase,
    val getPersonTvCreditsUseCase: GetPersonTvCreditsUseCase,
) : BaseViewModel<PersonContract.Event, PersonContract.State, PersonContract.Effect>() {
    private val id: Int = checkNotNull(savedStateHandle["id"])

    override fun setInitialState() = PersonContract.State()

    override fun handleEvents(event: PersonContract.Event) {

    }

    init {
        viewModelScope.launch {
            getNetworkConnectivityStateUseCase().collectLatest { state ->
                if (
                    state == NetworkStatus.Connected
                ) {
                    setState { copy(loading = true, connected = state) }
                    updatePersonDetails()
                    setState { copy(loading = false) }
                } else
                    setState { copy(connected = state) }
            }
        }
    }

    private suspend fun updatePersonDetails() {
        val personResult = getPersonDetailsUseCase(id)
        if (personResult.isFailure)
            return
        setState { copy(person = personResult.getOrNull()) }

        val personImages = getPersonImagesUseCase(id)
        setState { copy(imageList = personImages.getOrNull()) }

        val personMovieCredits = getPersonMovieCreditsUseCase(id)
        setState { copy(movieCredits = personMovieCredits.getOrNull()) }

        val personTvCredits = getPersonTvCreditsUseCase(id)
        setState { copy(tvCredits = personTvCredits.getOrNull()) }
    }
}
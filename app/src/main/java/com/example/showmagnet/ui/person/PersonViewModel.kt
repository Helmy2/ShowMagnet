package com.example.showmagnet.ui.person

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.showmagnet.domain.model.common.NetworkUnavailableException
import com.example.showmagnet.domain.use_case.person.AddPersonToFavoriteUseCase
import com.example.showmagnet.domain.use_case.person.GetPersonDetailsUseCase
import com.example.showmagnet.domain.use_case.person.GetPersonImagesUseCase
import com.example.showmagnet.domain.use_case.person.GetPersonMovieCreditsUseCase
import com.example.showmagnet.domain.use_case.person.GetPersonTvCreditsUseCase
import com.example.showmagnet.domain.use_case.person.RemovePersonFromFavoriteUseCase
import com.example.showmagnet.ui.common.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PersonViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    val getPersonImagesUseCase: GetPersonImagesUseCase,
    val getPersonDetailsUseCase: GetPersonDetailsUseCase,
    val getPersonMovieCreditsUseCase: GetPersonMovieCreditsUseCase,
    val getPersonTvCreditsUseCase: GetPersonTvCreditsUseCase,
    val addPersonToFavoriteUseCase: AddPersonToFavoriteUseCase,
    val removePersonFromFavoriteUseCase: RemovePersonFromFavoriteUseCase
) : BaseViewModel<PersonContract.Event, PersonContract.State, PersonContract.Effect>() {
    private val id: Int = checkNotNull(savedStateHandle["id"])

    override fun setInitialState() = PersonContract.State()

    override fun handleEvents(event: PersonContract.Event) {
        when (event) {
            PersonContract.Event.Refresh -> refresh()
            PersonContract.Event.ToggleFavorite -> toggleFavorite()
        }
    }

    private fun toggleFavorite() = viewModelScope.launch {
        viewState.value.person?.favorite?.let {
            setState { copy(person = viewState.value.person?.copy(favorite = !it)) }
            if (!it) {
                addPersonToFavoriteUseCase(id)
            } else {
                removePersonFromFavoriteUseCase(id)
            }
            updatePersonDetails()
        }
    }

    private suspend fun updatePersonDetails() {
        val personResult = getPersonDetailsUseCase(id)

        if (personResult.isFailure) {
            if (personResult.exceptionOrNull() is NetworkUnavailableException) setState {
                copy(loading = false, connected = false)
            }
            else {
                setEffect {
                    PersonContract.Effect.ShowErrorToast(
                        personResult.exceptionOrNull()?.localizedMessage ?: ""
                    )
                }
            }
            return
        }

        setState {
            copy(
                person = personResult.getOrNull(), connected = true, loading = false
            )
        }
    }

    init {
        refresh()
    }

    private fun refresh() = viewModelScope.launch {
        setState { copy(loading = true) }

        updatePersonDetails()

        val personImages = getPersonImagesUseCase(id)
        setState {
            copy(
                imageList = personImages.getOrNull(), connected = true, loading = false
            )
        }

        val personMovieCredits = getPersonMovieCreditsUseCase(id)
        setState {
            copy(
                movieCredits = personMovieCredits.getOrNull(), connected = true, loading = false
            )
        }

        val personTvCredits = getPersonTvCreditsUseCase(id)
        setState {
            copy(
                tvCredits = personTvCredits.getOrNull(), connected = true, loading = false
            )
        }

        delay(500)
        setState { copy(loading = false) }
    }
}
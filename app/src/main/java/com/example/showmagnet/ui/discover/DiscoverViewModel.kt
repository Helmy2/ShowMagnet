package com.example.showmagnet.ui.discover

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.showmagnet.domain.model.common.Genre
import com.example.showmagnet.domain.model.common.MediaType
import com.example.showmagnet.domain.model.common.NetworkUnavailableException
import com.example.showmagnet.domain.model.common.SortBy
import com.example.showmagnet.domain.use_case.show.DiscoverUseCase
import com.example.showmagnet.domain.use_case.show.SearchUseCase
import com.example.showmagnet.ui.common.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiscoverViewModel @Inject constructor(
    private val discoverUseCase: DiscoverUseCase, private val searchUseCase: SearchUseCase
) : BaseViewModel<DiscoverContract.Event, DiscoverContract.State, DiscoverContract.Effect>() {
    override fun setInitialState() = DiscoverContract.State()

    private val pageNumber = mutableStateOf(1)

    override fun handleEvents(event: DiscoverContract.Event) {
        when (event) {
            DiscoverContract.Event.Refresh -> {
                pageNumber.value = 1
                if (viewState.value.search.isBlank())
                    refresh()
                else
                    search()
            }

            DiscoverContract.Event.LoadMore -> {
                pageNumber.value++
                if (viewState.value.search.isBlank())
                    refresh()
                else
                    search()
            }

            is DiscoverContract.Event.MediaTypeChange -> {
                pageNumber.value = 1
                changeMediaType(
                    event.mediaType, event.genre, event.adult, event.sortBy
                )
            }

            is DiscoverContract.Event.SearchValueChange -> {
                setState { copy(search = event.query) }
                if (event.query.isBlank()) {
                    pageNumber.value = 1
                    refresh()
                }
            }

            DiscoverContract.Event.Search -> {
                pageNumber.value = 1
                search()
            }
        }
    }

    private fun search() = viewModelScope.launch {
        if (viewState.value.search.isNotBlank()) {
            setState { copy(loading = true) }
            val result = searchUseCase(
                page = pageNumber.value,
                mediaType = viewState.value.mediaType,
                query = viewState.value.search
            )
            if (result.isSuccess) setState {
                copy(
                    shows = if (pageNumber.value == 1) result.getOrDefault(emptyList())
                    else viewState.value.shows + result.getOrDefault(emptyList()),
                    loading = false,
                    connected = true
                )
            } else {
                if (result.exceptionOrNull() is NetworkUnavailableException) setState {
                    copy(connected = false, loading = false)
                }
                setState { copy(loading = false) }
            }
        }
    }


    private fun changeMediaType(
        mediaType: MediaType, genre: Genre?, adult: Boolean, sortBy: SortBy
    ) {
        setState { copy(mediaType = mediaType, genre = genre, adult = adult, sortBy = sortBy) }
        if (viewState.value.search.isBlank())
            refresh()
        else
            search()
    }

    private fun refresh() = viewModelScope.launch {
        val result = discoverUseCase(
            page = pageNumber.value,
            mediaType = viewState.value.mediaType,
            genreId = viewState.value.genre?.id,
            adult = viewState.value.adult,
            sortBy = viewState.value.sortBy
        )
        if (result.isSuccess) setState {
            copy(
                shows = if (pageNumber.value == 1) result.getOrDefault(emptyList())
                else viewState.value.shows + result.getOrDefault(emptyList()),
                connected = true,
                loading = false
            )
        } else {
            if (result.exceptionOrNull() is NetworkUnavailableException) setState {
                copy(connected = false, loading = false)
            }
            setState { copy(loading = false) }
        }
    }

    init {
        setState { copy(loading = true) }
        refresh()
    }
}
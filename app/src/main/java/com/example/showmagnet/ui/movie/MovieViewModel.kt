package com.example.showmagnet.ui.movie

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.showmagnet.domain.use_case.movie.GetCastUseCase
import com.example.showmagnet.domain.use_case.movie.GetCollectionUseCase
import com.example.showmagnet.domain.use_case.movie.GetImagesUseCase
import com.example.showmagnet.domain.use_case.movie.GetMovieDetailsUseCase
import com.example.showmagnet.domain.use_case.movie.GetRecommendationsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    val getMovieDetailsUseCase: GetMovieDetailsUseCase,
    val getCastUseCase: GetCastUseCase,
    val getCollectionUseCase: GetCollectionUseCase,
    val getImagesUseCase: GetImagesUseCase,
    val getRecommendationsUseCase: GetRecommendationsUseCase
) : ViewModel() {
    private val id: Int = checkNotNull(savedStateHandle["id"])

    init {
        viewModelScope.launch {
            val movieDetails = getMovieDetailsUseCase(id)
            Log.d("MovieViewModel", ": $movieDetails")

            movieDetails.getOrNull()?.collectionId?.let {
                Log.d(
                    "MovieViewModel",
                    ": ${getCollectionUseCase(it)}"
                )
            }

//            Log.d("MovieViewModel", ": ${getCastUseCase(id)}")
//            Log.d("MovieViewModel", ": ${getImagesUseCase(id)}")
//            Log.d("MovieViewModel", ": ${getRecommendationsUseCase(id)}")
        }
    }
}
package com.example.showmagnet.domain.use_case.favorite

import com.example.showmagnet.domain.repository.TvRepository
import javax.inject.Inject

class GetFavoriteTvUseCase @Inject constructor(
    private val repository: TvRepository
) {
    suspend operator fun invoke() = repository.getFavoriteTv()
}
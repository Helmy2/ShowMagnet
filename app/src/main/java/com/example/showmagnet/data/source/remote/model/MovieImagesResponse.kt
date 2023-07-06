package com.example.showmagnet.data.source.remote.model

data class MovieImagesResponse(
    val backdrops: List<ImageRemote>,
    val posters: List<ImageRemote>,
)


package com.example.showmagnet.data.source.remote.model.movie

import com.example.showmagnet.data.source.remote.model.ImageRemote

data class ImagesResponse(
    val backdrops: List<ImageRemote>,
    val posters: List<ImageRemote>,
)


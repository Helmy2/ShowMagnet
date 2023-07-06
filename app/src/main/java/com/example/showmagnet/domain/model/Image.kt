package com.example.showmagnet.domain.model

data class Image(
    private val url: String? = "",
    val ratio: Float = .7f,
) {
    companion object {
        private const val IMAGE_URL_W500 = "https://image.tmdb.org/t/p/w500/"
        private const val IMAGE_URL_ORIGINAL = "https://image.tmdb.org/t/p/original/"
    }

    val baseUrl: String
        get() = IMAGE_URL_W500 + url

}
package com.example.showmagnet.data.source.remote.api

object ApiConstants {
    const val BASE_ENDPOINT = "https://api.themoviedb.org/3/"

    object Movie {
        const val DETAILS_ENDPOINT = "movie/{${ApiParameters.ID}}"
        const val CREDITS_ENDPOINT = "movie/{${ApiParameters.ID}}/credits"
        const val RECOMMENDATIONS_ENDPOINT = "movie/{${ApiParameters.ID}}/recommendations"
        const val COLLECTION_ENDPOINT = "collection/{${ApiParameters.ID}}"
        const val IMAGES_ENDPOINT = "movie/{${ApiParameters.ID}}/images"
        const val POPULAR_ENDPOINT = "movie/popular"
        const val UPCOMING_ENDPOINT = "movie/upcoming"
        const val SEARCH_ENDPOINT = "search/movie"
        const val TRENDING_ENDPOINT = "trending/movie/day"
    }

    object Person {
        const val DETAILS_ENDPOINT = "person/{${ApiParameters.ID}}"
        const val MOVIE_CREDITS_ENDPOINT = "person/{${ApiParameters.ID}}/movie_credits"
        const val TV_CREDITS_ENDPOINT = "person/{${ApiParameters.ID}}/tv_credits"
        const val IMAGES_ENDPOINT = "person/{${ApiParameters.ID}}/images"
        const val TRENDING_ENDPOINT = "trending/person/{${ApiParameters.TIME_WINDOW}}"
    }

    object Tv {
        const val DETAILS_ENDPOINT = "tv/{${ApiParameters.ID}}"
        const val CREDITS_ENDPOINT = "tv/{${ApiParameters.ID}}/credits"
        const val RECOMMENDATIONS_ENDPOINT = "tv/{${ApiParameters.ID}}/recommendations"
        const val IMAGES_ENDPOINT = "tv/{${ApiParameters.ID}}/images"
        const val SEASON_ENDPOINT =
            "tv/{${ApiParameters.ID}}/season/{${ApiParameters.SEASON_NUMBER}}"
        const val POPULAR_ENDPOINT = "tv/popular"
        const val SEARCH_ENDPOINT = "search/tv"
        const val TRENDING_ENDPOINT = "trending/tv/day"
    }

    object Discover {
        const val MOVIE_ANIME = "discover/movie?with_genres=16"
        const val TV_ANIME = "discover/tv?with_genres=16"
        const val MOVIE = "discover/movie"
        const val TV = "discover/tv"
    }
}


object ApiParameters {
    const val ID = "id"
    const val TIME_WINDOW = "time_window"
    const val SEASON_NUMBER = "season_number"
    const val QUERY = "query"
    const val PAGE = "page"
    const val FIRST_AIR_DATE_START = "first_air_date.gte"
    const val FIRST_AIR_DATE_END = "first_air_date.lte"
}
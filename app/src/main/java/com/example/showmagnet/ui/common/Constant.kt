package com.example.showmagnet.ui.common

import com.example.showmagnet.domain.model.common.Genre
import com.example.showmagnet.domain.model.common.SortBy

object Constant {
    val sortByList = listOf(SortBy.Popularity, SortBy.VoteCount, SortBy.VoteAverage)


    val tvGenreList = listOf(
        Genre(
            id = 10759, name = "Action & Adventure"
        ), Genre(
            id = 16, name = "Animation"
        ), Genre(
            id = 35, name = "Comedy"
        ), Genre(
            id = 80, name = "Crime"
        ), Genre(
            id = 99, name = "Documentary"
        ), Genre(
            id = 18, name = "Drama"
        ), Genre(
            id = 10751, name = "Family"
        ), Genre(
            id = 10762, name = "Kids"
        ), Genre(
            id = 9648, name = "Mystery"
        ), Genre(
            id = 10763, name = "News"
        ), Genre(
            id = 10764, name = "Reality"
        ), Genre(
            id = 10765, name = "Sci-Fi & Fantasy"
        ), Genre(
            id = 10766, name = "Soap"
        ), Genre(
            id = 10767, name = "Talk"
        ), Genre(
            id = 10768, name = "War & Politics"
        ), Genre(
            id = 37, name = "Western"
        )
    )

    val movieGenreList = listOf(
        Genre(
            28,
            "Action"
        ),
        Genre(
            12,
            "Adventure"
        ),
        Genre(
            16,
            "Animation"
        ),
        Genre(
            35,
            "Comedy"
        ),
        Genre(
            80,
            "Crime"
        ),
        Genre(
            99,
            "Documentary"
        ),
        Genre(
            18,
            "Drama"
        ),
        Genre(
            10751,
            "Family"
        ),
        Genre(
            14,
            "Fantasy"
        ),
        Genre(
            36,
            "History"
        ),
        Genre(
            27,
            "Horror"
        ),
        Genre(
            10402,
            "Music"
        ),
        Genre(
            9648,
            "Mystery"
        ),
        Genre(
            10749,
            "Romance"
        ),
        Genre(
            878,
            "Science Fiction"
        ),
        Genre(
            10770,
            "TV Movie"
        ),
        Genre(
            53,
            "Thriller"
        ),
        Genre(
            10752,
            "War"
        ),
        Genre(
            37,
            "Western"
        )
    )
}
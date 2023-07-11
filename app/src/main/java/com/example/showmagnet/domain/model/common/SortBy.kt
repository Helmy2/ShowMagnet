package com.example.showmagnet.domain.model.common

sealed class SortBy(private val route:String,val name: String) {

    fun query() = "$route.desc"

    object Popularity : SortBy("popularity","Popularity")
    object VoteCount : SortBy("vote_count","Vote count")
    object VoteAverage : SortBy("vote_average","Vote average")
}
package com.example.showmagnet.data.source.remote.database

sealed class Types(val value: String) {
    object MOVIES : Types("movies")
    object TVS : Types("tvs")
    object PERSONS : Types("persons")
}
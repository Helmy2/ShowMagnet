package com.example.showmagnet.data.source.remote.model.person

import com.example.showmagnet.data.source.remote.model.show.ShowRemote

data class PersonCreditResponse(
    val cast: List<ShowRemote>?,
    val crow: List<ShowRemote>?
)
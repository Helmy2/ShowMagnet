package com.example.showmagnet.domain.model.person

import com.example.showmagnet.domain.model.common.Image
import com.example.showmagnet.domain.model.common.TimeWindow

data class Person(
    val id: Int,
    val name: String,
    val profilePath: Image,
    val timeWindow: TimeWindow,
)
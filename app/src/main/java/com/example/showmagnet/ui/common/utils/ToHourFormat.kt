package com.example.showmagnet.ui.common.utils

fun Int.toHourFormat(): String {
    var minute = this
    var hour = 0
    while (minute > 60) {
        minute -= 60
        hour++
    }
    if (hour == 0) return "${minute}min"
    return "${hour}h ${minute}min"
}
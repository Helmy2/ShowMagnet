package com.example.showmagnet.ui.common.utils

sealed class NetworkStatus {
    object Unknown : NetworkStatus()
    object Connected : NetworkStatus()
    object Disconnected : NetworkStatus()
}
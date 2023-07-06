package com.example.showmagnet.domain.use_case

import com.example.showmagnet.ui.common.utils.NetworkConnectivityService
import com.example.showmagnet.ui.common.utils.NetworkStatus
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNetworkConnectivityStateUseCase @Inject constructor(
    private val connectivityService: NetworkConnectivityService
) {
    operator fun invoke(): Flow<NetworkStatus> {
        return connectivityService.networkStatus
    }
}
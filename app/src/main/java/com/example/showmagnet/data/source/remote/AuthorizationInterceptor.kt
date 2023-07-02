package com.example.showmagnet.data.source.remote

import com.example.showmagnet.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

object AuthorizationInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestWithHeader = chain.request()
            .newBuilder()
            .header(
                "Authorization", "Bearer ${BuildConfig.ACCESS_TOKEN}"
            ).build()
        return chain.proceed(requestWithHeader)
    }
}
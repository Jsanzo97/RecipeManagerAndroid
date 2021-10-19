package com.jsanzo97.recipemanager.di.common

import okhttp3.OkHttpClient
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.util.concurrent.TimeUnit

private const val AUTHORIZATION_HEADER = "Authorization"
private const val TIMEOUT = 15L

internal const val BASIC_OK_HTTP_CLIENT = "basic_ok_http_client"
internal const val UNAUTHENTICATED_OK_HTTP_CLIENT = "unauthenticated_ok_http_client"

val commonHttpModule = module {

    single(named(BASIC_OK_HTTP_CLIENT)) {
        OkHttpClient.Builder()
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
            .build()
    }
}

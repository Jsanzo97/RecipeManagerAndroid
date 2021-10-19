package com.jsanzo97.recipemanager.di.common

import okhttp3.OkHttpClient
import org.koin.core.qualifier.named
import org.koin.dsl.module

val httpModule = module {
    single(named(UNAUTHENTICATED_OK_HTTP_CLIENT)) {
        get<OkHttpClient>(named(BASIC_OK_HTTP_CLIENT))
    }
}

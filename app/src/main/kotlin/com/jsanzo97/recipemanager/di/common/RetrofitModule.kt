package com.jsanzo97.recipemanager.di.common

import com.jsanzo97.recipemanager.BuildConfig
import okhttp3.OkHttpClient
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

internal const val UNAUTHENTICATED_WS = "unauthenticated_ws"

val retrofitModule = module {

    single(named(UNAUTHENTICATED_WS)) { provideRetrofit(get(named(UNAUTHENTICATED_OK_HTTP_CLIENT))) }

}

private fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
    .client(okHttpClient)
    .baseUrl(BuildConfig.SERVER_ENDPOINT)
    .addConverterFactory(MoshiConverterFactory.create())
    .build()

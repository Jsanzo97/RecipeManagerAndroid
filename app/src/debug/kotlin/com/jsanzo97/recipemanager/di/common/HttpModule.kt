package com.jsanzo97.recipemanager.di.common

import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.github.simonpercic.oklog3.OkLogInterceptor
import com.jsanzo97.common.Logger
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.util.Locale.getDefault

private const val OKHTTPCLIENT_TAG = "OkHttp"

val httpModule = module {

    single { ChuckerInterceptor(androidContext()) }

    single(named(UNAUTHENTICATED_OK_HTTP_CLIENT)) {
        val logger = get<Logger>().tag(OKHTTPCLIENT_TAG)
        val basicOkHttpClient = get<OkHttpClient>(named(BASIC_OK_HTTP_CLIENT))
        val chuckerInterceptor = get<ChuckerInterceptor>()

        val loggingInterceptor = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                logger.v(message)
            }
        }).apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val okLogInterceptor = OkLogInterceptor.builder()
            .withAllLogData()
            .build()

        val languageInterceptor = Interceptor {
            it.proceed(it.request().newBuilder()
                .header("Accept-Language", getDefault().language)
                .build())
        }

        basicOkHttpClient.newBuilder()
            .addInterceptor(chuckerInterceptor)
            .addInterceptor(loggingInterceptor)
            .addInterceptor(okLogInterceptor)
            .addInterceptor(languageInterceptor)
            .build()
    }
}

package com.jsanzo97.recipemanager

import com.jakewharton.threetenabp.AndroidThreeTen
import com.jsanzo97.common.Logger
import com.jsanzo97.common_android.logger.TimberLogger
import com.jsanzo97.recipemanager.di.KoinLogger
import com.jsanzo97.recipemanager.di.book.bookModule
import com.jsanzo97.recipemanager.di.common.commonHttpModule
import com.jsanzo97.recipemanager.di.common.dataModule
import com.jsanzo97.recipemanager.di.common.httpModule
import com.jsanzo97.recipemanager.di.common.retrofitModule
import com.jsanzo97.recipemanager.di.creation.creationModule
import com.jsanzo97.recipemanager.di.login.loginModule
import com.jsanzo97.recipemanager.di.main.mainModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class RecipeManagerApp : CustomApp() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            val logger: Logger = TimberLogger()

            this.logger(KoinLogger(logger, Level.DEBUG))

            androidContext(this@RecipeManagerApp)

            koin.declare(logger)

            modules(
                listOf(
                    commonHttpModule,
                    retrofitModule,
                    httpModule,
                    dataModule,
                    loginModule,
                    mainModule,
                    bookModule,
                    creationModule
                )
            )

            AndroidThreeTen.init(applicationContext)
        }
    }
}

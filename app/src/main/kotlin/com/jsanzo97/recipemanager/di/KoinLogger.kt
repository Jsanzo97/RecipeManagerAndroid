package com.jsanzo97.recipemanager.di

import com.jsanzo97.common.Logger
import org.koin.core.logger.KOIN_TAG
import org.koin.core.logger.Level
import org.koin.core.logger.MESSAGE
import org.koin.core.logger.Logger as LoggerForKoin

class KoinLogger(logger: Logger, level: Level = Level.INFO) : LoggerForKoin(level) {

    private val logger: Logger = logger.tag(KOIN_TAG)

    override fun log(level: Level, msg: MESSAGE) {
        if (this.level <= level) {
            logOnLevel(msg)
        }
    }

    private fun logOnLevel(msg: MESSAGE) {
        when (this.level) {
            Level.DEBUG -> logger.d(msg)
            Level.INFO -> logger.i(msg)
            Level.ERROR -> logger.e(msg)
        }
    }
}

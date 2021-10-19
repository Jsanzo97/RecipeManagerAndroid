package com.jsanzo97.common_android.logger

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.jsanzo97.common.Logger

class LifecycleLogger(private val logger: Logger) : LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun logCreate() {
        logger.d("➡️ ON_CREATE")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun logStart() {
        logger.d("➡️ ON_START")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun logResume() {
        logger.d("➡️ ON_RESUME")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun logPause() {
        logger.d("➡️ ON_PAUSE")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun logStop() {
        logger.d("➡️ ON_STOP")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun logDestroy() {
        logger.d("➡️ ON_DESTROY")
    }
}

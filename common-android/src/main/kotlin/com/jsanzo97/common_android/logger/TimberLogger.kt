package com.jsanzo97.common_android.logger

import com.jsanzo97.common.Logger
import timber.log.Timber

class TimberLogger(private val tag: String? = null) : Logger {

    override fun v(message: String, vararg args: Any) = timber.v(message, *args)

    override fun v(t: Throwable, message: String, vararg args: Any) = timber.v(t, message, *args)

    override fun v(t: Throwable) = timber.v(t)

    override fun d(message: String, vararg args: Any) = timber.d(message, *args)

    override fun d(t: Throwable, message: String, vararg args: Any) = timber.d(t, message, *args)

    override fun d(t: Throwable) = timber.d(t)

    override fun i(message: String, vararg args: Any) = timber.i(message, *args)

    override fun i(t: Throwable, message: String, vararg args: Any) = timber.i(t, message, *args)

    override fun i(t: Throwable) = timber.i(t)

    override fun w(message: String, vararg args: Any) = timber.w(message, *args)

    override fun w(t: Throwable, message: String, vararg args: Any) = timber.w(t, message, *args)

    override fun w(t: Throwable) = timber.w(t)

    override fun e(message: String, vararg args: Any) = timber.e(message, *args)

    override fun e(t: Throwable, message: String, vararg args: Any) = timber.e(t, message, *args)

    override fun e(t: Throwable) = timber.e(t)

    override fun wtf(message: String, vararg args: Any) = timber.wtf(message, *args)

    override fun wtf(t: Throwable, message: String, vararg args: Any) =
        timber.wtf(t, message, *args)

    override fun wtf(t: Throwable) = timber.w(t)

    override fun tag(tag: String): Logger = TimberLogger(tag)

    private val timber: Timber.Tree
        get() = if (tag == null) Timber.asTree() else Timber.tag(tag)
}

package com.jsanzo97.data.error

sealed class LocalDataError

object WritingError : LocalDataError()
object ReadingError : LocalDataError()
object IOError : LocalDataError()
object ElementNotFoundError : LocalDataError()
object InvalidArgumentError : LocalDataError()

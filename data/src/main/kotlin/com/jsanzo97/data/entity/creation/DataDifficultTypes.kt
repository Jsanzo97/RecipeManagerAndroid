package com.jsanzo97.data.entity.creation

import com.jsanzo97.domain.entity.creation.DifficultType

data class DataDifficultTypes (
    val difficult: String
)

fun DataDifficultTypes.toDifficultType() = DifficultType(difficult)
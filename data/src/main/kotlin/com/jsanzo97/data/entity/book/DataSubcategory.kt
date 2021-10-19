package com.jsanzo97.data.entity.book

import com.jsanzo97.domain.entity.book.Subcategory

data class DataSubcategory (
    val value: String
)

fun DataSubcategory.toSubcategory() = Subcategory (
    value
)

fun Subcategory.toDataSubcategory() = DataSubcategory(
    value
)
package com.jsanzo97.common_android.ui.extensions

fun List<String>.toHtmlList(): String {
    var formattedValue = "<ul>"

    forEach {
        formattedValue += "<li> "
        formattedValue += it
        formattedValue += "</li>"
    }

    formattedValue += "</ul>"

    return formattedValue
}
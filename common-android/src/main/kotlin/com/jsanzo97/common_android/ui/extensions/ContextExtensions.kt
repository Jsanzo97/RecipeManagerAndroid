package com.jsanzo97.common_android.ui.extensions

import android.content.Context
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.StyleRes
import androidx.core.content.res.ResourcesCompat
import arrow.core.Option

fun Context.getActionBarSize(): Int {
    val tv = TypedValue()
    return if (theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
        TypedValue.complexToDimensionPixelSize(tv.data, resources.displayMetrics)
    } else {
        0
    }
}

@ColorInt
fun Context.getColorCompat(@ColorRes colorId: Int) =
    ResourcesCompat.getColor(resources, colorId, null)

@ColorInt
fun Context.getColorFromStyle(@StyleRes style: Int, @AttrRes attr: Int, @ColorInt defaultColor: Int): Int {
    val ta = obtainStyledAttributes(style, intArrayOf(attr))
    val themeColor = ta.getColor(0, defaultColor)
    ta.recycle()
    return themeColor
}

fun Context.getColorOrDefault(optionColor: Option<Int>, @ColorInt colorInt: Int) =
    optionColor.fold({ colorInt }, { getColorCompat(it) })

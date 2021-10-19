package com.jsanzo97.common_android.ui.extensions

import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import androidx.core.view.forEach
import androidx.core.view.isVisible


fun ViewGroup.dispatchApplyWindowInsetsToAllChildren(insets: WindowInsets): WindowInsets {
    var consumed = false
    forEach { child ->
        // Dispatch the insets to the child
        val childResult = child.dispatchApplyWindowInsets(insets)
        // If the child consumed the insets, record it
        if (childResult.isConsumed) {
            consumed = true
        }
    }
    // If any of the children consumed the insets, return
    // an appropriate value
    return if (consumed) insets.consumeSystemWindowInsets() else insets
}

fun View.changeVisibility(visible: Boolean) {
    if (this.isVisible == visible) {
        return
    }

    this.visibility = if (visible) View.VISIBLE else View.GONE
}

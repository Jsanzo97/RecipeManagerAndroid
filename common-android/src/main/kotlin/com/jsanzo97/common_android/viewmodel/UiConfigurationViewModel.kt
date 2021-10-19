package com.jsanzo97.common_android.viewmodel

import android.view.MotionEvent
import android.view.WindowInsets
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import arrow.core.None
import arrow.core.Option
import arrow.core.or
import arrow.core.some
import com.jsanzo97.common_android.ui.StateHandler
import com.jsanzo97.common_android.ui.StateProvider
import com.jsanzo97.common_android.ui.UIConfigurationViewState

data class UiConfigurationViewState(
    var showToolbar: Boolean = false,
    var statusBarColor: Option<Int> = None,
    var toolbarColor: Option<Int> = None,
    var toolbarNavigationIconColor: Option<Int> = None,
    var insets: Option<WindowInsets> = None,
    var showLogoutButton: Boolean = false
) : UIConfigurationViewState

class UiConfigurationViewModel : ViewModel(),
    StateProvider<UiConfigurationViewState> by StateHandler() {

    fun updateViewState(viewState: UiConfigurationViewState) {
        val newViewState = currentState.fold(
            ifEmpty = { viewState },
            ifSome = { currentState ->
                viewState.copy(insets = viewState.insets.or(currentState.insets))
            }
        )
        postState(newViewState)
    }

    fun setInsets(insets: WindowInsets) {
        val newViewState = currentState.fold(
            ifEmpty = { UiConfigurationViewState(insets = insets.some()) },
            ifSome = { currentState ->
                currentState.copy(insets = insets.some())
            }
        )
        postState(newViewState)
    }

    val toolbarMotion: MutableLiveData<MotionEvent> = MutableLiveData()

}

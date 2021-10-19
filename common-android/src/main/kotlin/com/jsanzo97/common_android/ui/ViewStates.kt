package com.jsanzo97.common_android.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import arrow.core.Option
import arrow.core.toOption

interface ViewState: State {
    val genericError: Boolean
    val genericMessage: String
    val specificError: Boolean
    val specificMessageId: Int
    val loading: Boolean
}

interface UIConfigurationViewState: State

interface State

interface ViewAction

interface StateProvider<BaseViewState : State> {

    val currentState: Option<BaseViewState>

    val states: LiveData<BaseViewState>

    fun postState(state: BaseViewState)
}

interface ViewActionProcessor<in BaseViewAction : ViewAction> {

    fun processAction(viewAction: BaseViewAction)
}

class StateHandler<BaseViewState : State> :
    StateProvider<BaseViewState> {

    private val _states: MutableLiveData<BaseViewState> = MutableLiveData()

    override val currentState: Option<BaseViewState>
        get() = _states.value.toOption()

    override val states: LiveData<BaseViewState>
        get() = _states

    override fun postState(state: BaseViewState) = _states.postValue(state)
}


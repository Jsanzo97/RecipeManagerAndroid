package com.jsanzo97.common_android.ui.extensions

import android.view.View
import androidx.annotation.IdRes
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import com.jsanzo97.common_android.ui.CustomFragment

inline fun <T : Any, L : LiveData<T>> FragmentActivity.observe(
    liveData: L,
    crossinline body: (T) -> Unit
) {
    liveData.observe(this, Observer { viewState -> viewState?.let(body) })
}

inline fun <T : Any, L : LiveData<T>> CustomFragment.observe(liveData: L, crossinline body: (T) -> Unit) {
    liveData.observe(viewLifecycleOwner, Observer { viewState -> viewState?.let(body) })
}

fun <T : View> CustomFragment.bindView(@IdRes idRes: Int): T = requireView().findViewById(idRes)

fun <T : View> CustomFragment.lazyBindView(@IdRes idRes: Int): LazyFragmentViewBinder<T> {
    return LazyFragmentViewBinder(this) { this.bindView<T>(idRes) }
}

class LazyFragmentViewBinder<out T>(
    private val fragment: CustomFragment,
    private val initializer: (CustomFragment) -> T
) : Lazy<T>, LifecycleObserver {
    private object EMPTY

    private var _value: Any? = EMPTY

    init {
        fragment.viewLifecycleOwnerLiveData.observe(fragment, Observer {
            it.lifecycle.addObserver(this)
        })
    }

    override val value: T
        get() {
            if (EMPTY === _value) {
                _value = initializer(fragment)
            }

            @Suppress("UNCHECKED_CAST")
            return _value as T
        }

    override fun isInitialized(): Boolean = EMPTY === _value

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun reset() {
        _value = EMPTY
    }
}

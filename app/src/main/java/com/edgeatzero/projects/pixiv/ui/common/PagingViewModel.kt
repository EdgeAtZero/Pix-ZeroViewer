package com.edgeatzero.projects.pixiv.ui.common

import android.app.Application
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.edgeatzero.library.model.Controller
import com.edgeatzero.library.model.LoadState

abstract class PagingViewModel<T>(application: Application) : BasicViewModel(application) {

    protected val controller = MediatorLiveData<Controller<T>>()

    val state = controller.switchMap { it.state.invoke() }

    val data = controller.switchMap { it.data }

    val isLoaded
        get() = controller.value != null

    @Suppress("UNCHECKED_CAST")
    protected fun getState(): MutableLiveData<LoadState>? {
        return state as? MutableLiveData<LoadState>
    }

    open fun load() {
        throw NotImplementedError()
    }

    open fun refresh() {
        controller.value?.refresh?.invoke()
    }

    open fun retry() {
        controller.value?.retry?.invoke()
    }

}

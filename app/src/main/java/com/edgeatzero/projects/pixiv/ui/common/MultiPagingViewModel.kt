@file:Suppress("UNCHECKED_CAST")

package com.edgeatzero.projects.pixiv.ui.common

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.edgeatzero.library.model.Controller
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class MultiPagingViewModel<Key, Initializer : Function<Controller<*>>>(
    application: Application
) : BasicViewModel(application) {

    protected open val controllers by lazy { HashMap<Key, MutableLiveData<Controller<*>>>() }

    protected open val initializes by lazy { HashMap<Key, Initializer>() }

    open fun <T> getController(
        key: Key,
        initialLoad: Boolean,
        initializer: Initializer
    ): LiveData<Controller<T>>? {
        if (initializes[key] == null) initializes[key] = initializer
        return controllers[key] as? MutableLiveData<Controller<T>>
            ?: MutableLiveData<Controller<T>>().also {
                controllers[key] = it as MutableLiveData<Controller<*>>
                if (initialLoad) load(key)
            }
    }

    open fun isLoad(key: Key?): Boolean {
        key ?: return false
        return controllers[key]?.value != null
    }

    open fun load(key: Key?) {
        key ?: return
        val initializer = initializes[key] ?: return
        viewModelScope.launch(Dispatchers.Default) {
            val load = processInitializer(initializer)
            withContext(Dispatchers.Main) {
                controllers[key]?.value = load
            }
        }
    }

    abstract suspend fun processInitializer(initializer: Initializer): Controller<*>

    fun refresh(key: Key?) {
        key ?: return
        controllers[key]?.value?.refresh?.invoke()
    }

    fun retry(key: Key?) {
        key ?: return
        controllers[key]?.value?.retry?.invoke()
    }

}

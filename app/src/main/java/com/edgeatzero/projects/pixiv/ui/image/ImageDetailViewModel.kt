package com.edgeatzero.projects.pixiv.ui.image

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import com.edgeatzero.library.base.BaseViewModel

class ImageDetailViewModel(
    application: Application,
    handle: SavedStateHandle
) : BaseViewModel(application) {

    companion object {

        const val KEY_URLS = "key_urls"
        const val KEY_CURRENT_ITEM = "key_current_item"

    }

    private val _urls = handle.getLiveData<List<String>>(KEY_URLS)
    val urls: LiveData<List<String>> = _urls

    private val _currentItem = handle.getLiveData<Int>(KEY_CURRENT_ITEM)
    val currentItem: LiveData<Int> = _currentItem

    fun postCurrentItem(item: Int) {
        _currentItem.value = item
    }

    fun postUrls(urls: List<String>) {
        _urls.value = urls
    }

}

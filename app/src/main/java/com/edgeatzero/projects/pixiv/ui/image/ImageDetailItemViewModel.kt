package com.edgeatzero.projects.pixiv.ui.image

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import com.edgeatzero.library.base.BaseViewModel

class ImageDetailItemViewModel(
    application: Application,
    handle: SavedStateHandle
) : BaseViewModel(application) {

    companion object {

        const val KEY_URL = "key_url"

    }

    private val _url = handle.getLiveData<String>(KEY_URL)
    val url: LiveData<String> = _url

    fun postUrl(url: String) {
        _url.value = url
    }

}

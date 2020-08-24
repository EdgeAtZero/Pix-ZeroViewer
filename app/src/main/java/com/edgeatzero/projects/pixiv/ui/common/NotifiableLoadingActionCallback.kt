package com.edgeatzero.projects.pixiv.ui.common

interface NotifiableLoadingActionCallback {

    val isEnableNotifiableLoadingAction: Boolean
        get() = false

    val isLoaded: Boolean

    fun onRequestLoad()

}

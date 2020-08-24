package com.edgeatzero.projects.pixiv.ui.illustration

import androidx.lifecycle.LiveData
import com.edgeatzero.library.model.ResponseState
import com.edgeatzero.projects.pixiv.model.Illustration

interface IllustrationToggleLike {

    fun toggleLike(illustration: Illustration): LiveData<ResponseState>

}

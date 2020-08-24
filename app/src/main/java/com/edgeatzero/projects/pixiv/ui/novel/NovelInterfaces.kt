package com.edgeatzero.projects.pixiv.ui.novel

import androidx.lifecycle.LiveData
import com.edgeatzero.library.model.ResponseState
import com.edgeatzero.projects.pixiv.model.Novel

interface NovelToggleLike {

    fun toggleLike(novel: Novel): LiveData<ResponseState>

}

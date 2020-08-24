package com.edgeatzero.projects.pixiv.ui.user

import androidx.lifecycle.LiveData
import com.edgeatzero.library.model.ResponseState
import com.edgeatzero.projects.pixiv.model.User

interface UserToggleFollow {

    fun toggleFollow(user: User): LiveData<ResponseState>

}

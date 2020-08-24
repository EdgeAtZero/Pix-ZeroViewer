package com.edgeatzero.projects.pixiv.ui.common

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.edgeatzero.library.base.BaseViewModel
import com.edgeatzero.library.ext.launchAsLiveData
import com.edgeatzero.library.model.ResponseState
import com.edgeatzero.projects.pixiv.http.repository.ApplicationRepository
import com.edgeatzero.projects.pixiv.model.Illustration
import com.edgeatzero.projects.pixiv.model.Novel
import com.edgeatzero.projects.pixiv.model.User
import com.edgeatzero.projects.pixiv.model.util.ViewModelUtils.toggleFollow
import com.edgeatzero.projects.pixiv.model.util.ViewModelUtils.toggleLike
import com.edgeatzero.projects.pixiv.ui.illustration.IllustrationToggleLike
import com.edgeatzero.projects.pixiv.ui.novel.NovelToggleLike
import com.edgeatzero.projects.pixiv.ui.user.UserToggleFollow
import kotlinx.coroutines.Dispatchers
import org.kodein.di.generic.instance

abstract class BasicViewModel(
    application: Application
) : BaseViewModel(application), IllustrationToggleLike, NovelToggleLike, UserToggleFollow {

    protected val applicationRepository by instance<ApplicationRepository>()

    override fun toggleLike(
        illustration: Illustration
    ) = viewModelScope.launchAsLiveData(Dispatchers.Main, initial = ResponseState.LOADING) { data ->
        data.value = applicationRepository.toggleLike(illustration)
    }

    override fun toggleLike(
        novel: Novel
    ) = viewModelScope.launchAsLiveData(Dispatchers.Main, initial = ResponseState.LOADING) { data ->
        data.value = applicationRepository.toggleLike(novel)
    }

    override fun toggleFollow(
        user: User
    ) = viewModelScope.launchAsLiveData(Dispatchers.Main, initial = ResponseState.LOADING) { data ->
        data.value = applicationRepository.toggleFollow(user)
    }

}

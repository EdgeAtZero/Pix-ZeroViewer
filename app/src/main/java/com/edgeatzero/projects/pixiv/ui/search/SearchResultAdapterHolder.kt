package com.edgeatzero.projects.pixiv.ui.search

import androidx.fragment.app.Fragment
import androidx.paging.PagedList
import androidx.recyclerview.widget.ConcatAdapter
import com.edgeatzero.projects.pixiv.model.Illustration
import com.edgeatzero.projects.pixiv.model.Novel
import com.edgeatzero.projects.pixiv.model.UserPreview
import com.edgeatzero.projects.pixiv.ui.common.AnimatorAdapter
import com.edgeatzero.projects.pixiv.ui.common.BasicViewModel
import com.edgeatzero.projects.pixiv.ui.illustration.IllustrationCommonAdapter
import com.edgeatzero.projects.pixiv.ui.novel.NovelCommonAdapter
import com.edgeatzero.projects.pixiv.ui.user.UserPreviewAdapter

class SearchResultAdapterHolder(fragment: Fragment, lambda: () -> BasicViewModel) {

    private val illustrations by lazy {
        IllustrationCommonAdapter(fragment = fragment, lambda = lambda)
    }

    private val novels by lazy {
        NovelCommonAdapter(fragment = fragment, lambda = lambda)
    }

    private val users by lazy {
        UserPreviewAdapter(fragment = fragment, lambda = lambda)
    }

    val adapter by lazy { AnimatorAdapter(ConcatAdapter(illustrations, novels, users)) }

    fun submitIllustrations(list: PagedList<Illustration>?) {
        illustrations.submitList(list)
    }

    fun submitNovels(list: PagedList<Novel>?) {
        novels.submitList(list)
    }

    fun submitUsers(list: PagedList<UserPreview>?) {
        users.submitList(list)
    }

}

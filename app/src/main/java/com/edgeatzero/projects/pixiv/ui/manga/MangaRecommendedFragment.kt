package com.edgeatzero.projects.pixiv.ui.manga

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.edgeatzero.library.ext.activityViewModels
import com.edgeatzero.projects.pixiv.constant.ContentType
import com.edgeatzero.projects.pixiv.ui.illustration.IllustrationRankingHeaderAdapter
import com.edgeatzero.projects.pixiv.ui.illustration.IllustrationRecommendedFragment
import com.edgeatzero.projects.pixiv.ui.main.RecommendedHeader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MangaRecommendedFragment : IllustrationRecommendedFragment() {

    override val model by activityViewModels<ViewModel>()

    override val header by lazy { RecommendedHeader(ContentType.MANGA) { IllustrationRankingHeaderAdapter() } }

    class ViewModel(
        application: Application
    ) : IllustrationRecommendedFragment.ViewModel(application) {

        override fun load() {
            viewModelScope.launch(Dispatchers.Default) {
                applicationRepository.mangaRecommended(original).let {
                    withContext(Dispatchers.Main) { controller.value = it }
                }
            }
        }

    }
}

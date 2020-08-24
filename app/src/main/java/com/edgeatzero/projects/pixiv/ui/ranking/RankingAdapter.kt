package com.edgeatzero.projects.pixiv.ui.ranking

import androidx.fragment.app.Fragment
import com.edgeatzero.library.common.BaseFragmentPagerAdapter
import com.edgeatzero.projects.pixiv.constant.ContentType
import com.edgeatzero.projects.pixiv.constant.RankingCategory
import com.edgeatzero.projects.pixiv.ui.illustration.IllustrationRankingFragment
import com.edgeatzero.projects.pixiv.ui.novel.NovelRankingFragment

class RankingAdapter(
    private val activity: RankingActivity
) : BaseFragmentPagerAdapter(activity.supportFragmentManager) {

    fun submitType(contentType: ContentType) {
        this.contentType = contentType
        data = when (contentType) {
            ContentType.ILLUSTRATION -> RankingCategory.illustration
            ContentType.MANGA -> RankingCategory.manga
            ContentType.NOVEL -> RankingCategory.novel
            else -> throw IndexOutOfBoundsException()
        }
        notifyDataSetChanged()
    }

    private var contentType: ContentType? = null

    private lateinit var data: Array<RankingCategory>

    override fun getPageTitle(position: Int): CharSequence? = activity.getString(data[position].res)

    override fun getItem(position: Int): Fragment {
        val data = data[position]
        return when (contentType) {
            ContentType.ILLUSTRATION, ContentType.MANGA -> IllustrationRankingFragment().apply {
                category = data
            }
            ContentType.NOVEL -> NovelRankingFragment().apply {
                category = data
            }
            else -> throw IndexOutOfBoundsException()
        }
    }

    override fun getCount(): Int = if (::data.isInitialized) data.size else 0

}

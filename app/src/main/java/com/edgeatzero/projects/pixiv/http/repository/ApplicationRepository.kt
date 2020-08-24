package com.edgeatzero.projects.pixiv.http.repository

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.paging.Config
import androidx.paging.PagedList
import com.edgeatzero.library.base.BaseRepository
import com.edgeatzero.library.ext.newController
import com.edgeatzero.library.model.LoadResult
import com.edgeatzero.projects.pixiv.constant.RankingCategory
import com.edgeatzero.projects.pixiv.constant.Restrict
import com.edgeatzero.projects.pixiv.http.service.PixivApplicationApiService
import com.edgeatzero.projects.pixiv.model.Illustration
import com.edgeatzero.projects.pixiv.model.Novel
import com.edgeatzero.projects.pixiv.util.nextUrlDataSource
import okhttp3.OkHttpClient
import org.kodein.di.generic.instance

class ApplicationRepository(
    application: Application,
    private val config: PagedList.Config = Config(pageSize = 10, enablePlaceholders = true)
) : BaseRepository(application) {

    private val applicationApiService by instance<PixivApplicationApiService>()

    private val client by instance<OkHttpClient>("Common")

    fun illustrationBookmarkAdd(
        id: Long,
        restrict: Restrict = Restrict.PUBLIC,
        tags: List<String>? = null
    ) = applicationApiService.illustrationBookmarkAdd(
        illust_id = id,
        restrict = restrict.toString(),
        tags = tags
    )

    fun illustrationBookmarkDelete(
        id: Long
    ) = applicationApiService.illustrationBookmarkDelete(
        illust_id = id
    )

    fun illustrationBookmarkDetail(
        id: Long
    ) = applicationApiService.illustrationBookmarkDetail(
        illust_id = id
    )

    fun illustrationCommentaryAdd(
        id: Long,
        comment: String,
        parent: Long? = null
    ) = applicationApiService.illustrationCommentaryAdd(
        illust_id = id,
        comment = comment,
        parent_comment_id = parent
    )

    fun illustrationCommentaryDelete(
        id: Long
    ) = applicationApiService.illustrationCommentaryDelete(
        comment_id = id
    )

    fun illustrationCommentaryReplies(
        id: Long
    ) = newController(config) {
        client.nextUrlDataSource(
            prepareCall = {
                applicationApiService.illustrationCommentaryReplies(comment_id = id)
            },
            processResponse = {
                LoadResult.Successful(it.comments, null, it.nextUrl)
            }
        )
    }

    fun illustrationCommentaries(
        id: Long
    ) = newController(config) {
        client.nextUrlDataSource(
            prepareCall = {
                applicationApiService.illustrationCommentaries(illust_id = id)
            },
            processResponse = {
                LoadResult.Successful(it.comments, null, it.nextUrl)
            }
        )
    }

    fun illustrationDetail(
        id: Long
    ) = applicationApiService.illustrationDetail(illust_id = id)

    fun illustrationRanking(
        category: RankingCategory,
        date: String? = null
    ) = newController(config) {
        client.nextUrlDataSource(
            prepareCall = {
                applicationApiService.illustrationRanking(mode = category.toString(), date = date)
            },
            processResponse = {
                LoadResult.Successful(it.Illustrations, null, it.nextUrl)
            }
        )
    }

    fun illustrationRelated(
        id: Long
    ) = newController(config) {
        client.nextUrlDataSource(
            prepareCall = {
                applicationApiService.illustrationRelated(illust_id = id)
            },
            processResponse = {
                LoadResult.Successful(it.Illustrations, null, it.nextUrl)
            }
        )
    }

    fun illustrationRecommended(
        header: MutableLiveData<List<Illustration>>? = null
    ) = newController(config) {
        client.nextUrlDataSource(
            prepareCall = {
                applicationApiService.illustrationRecommended(include_ranking_illusts = true)
            },
            processResponse = {
                if (!it.rankingIllustrations.isNullOrEmpty()) header?.postValue(it.rankingIllustrations)
                LoadResult.Successful(it.Illustrations, null, it.nextUrl)
            }
        )
    }

    fun mangaRecommended(
        header: MutableLiveData<List<Illustration>>? = null
    ) = newController(config) {
        client.nextUrlDataSource(
            prepareCall = {
                applicationApiService.mangaRecommended(include_ranking_illusts = true)
            },
            processResponse = {
                if (!it.rankingIllustrations.isNullOrEmpty()) header?.postValue(it.rankingIllustrations)
                LoadResult.Successful(it.Illustrations, null, it.nextUrl)
            }
        )
    }

    fun novelBookmarkAdd(
        id: Long,
        restrict: Restrict = Restrict.PUBLIC,
        tags: List<String>? = null
    ) = applicationApiService.novelBookmarkAdd(
        novel_id = id,
        restrict = restrict.toString(),
        tags = tags
    )

    fun novelBookmarkDelete(
        id: Long
    ) = applicationApiService.novelBookmarkDelete(
        novel_id = id
    )

    fun novelBookmarkDetail(
        id: Long
    ) = applicationApiService.novelBookmarkDetail(
        novel_id = id
    )

    fun novelCommentaryAdd(
        id: Long,
        comment: String,
        parent: Long? = null
    ) = applicationApiService.novelCommentaryAdd(
        novel_id = id,
        comment = comment,
        parent_comment_id = parent
    )

    fun novelCommentaryDelete(
        id: Long
    ) = applicationApiService.novelCommentaryDelete(
        comment_id = id
    )

    fun novelCommentaryReplies(
        id: Long
    ) = newController(config) {
        client.nextUrlDataSource(
            prepareCall = {
                applicationApiService.novelCommentaryReplies(comment_id = id)
            },
            processResponse = {
                LoadResult.Successful(it.comments, null, it.nextUrl)
            }
        )
    }

    fun novelCommentaries(
        id: Long
    ) = newController(config) {
        client.nextUrlDataSource(
            prepareCall = {
                applicationApiService.novelCommentaries(novel_id = id)
            },
            processResponse = {
                LoadResult.Successful(it.comments, null, it.nextUrl)
            }
        )
    }

    fun novelRanking(
        category: RankingCategory,
        date: String? = null
    ) = newController(config) {
        client.nextUrlDataSource(
            prepareCall = {
                applicationApiService.novelRanking(mode = category.toString(), date = date)
            },
            processResponse = {
                LoadResult.Successful(it.novels, null, it.nextUrl)
            }
        )
    }

    fun novelRecommended(
        header: MutableLiveData<List<Novel>>? = null
    ) = newController(config) {
        client.nextUrlDataSource(
            prepareCall = {
                applicationApiService.novelRecommended(include_ranking_novels = true)
            },
            processResponse = {
                if (!it.rankingNovels.isNullOrEmpty()) header?.postValue(it.rankingNovels)
                LoadResult.Successful(it.novels, null, it.nextUrl)
            }
        )
    }

    fun searchAutocomplete(
        keyword: String
    ) = applicationApiService.searchAutoComplete(word = keyword)

    fun searchIllustration(
        word: String,
        sort: String,
        target: String,
        minBookmark: Long? = null,
        maxBookmark: Long? = null,
        startDate: String? = null,
        endDate: String? = null
    ) = newController(config) {
        client.nextUrlDataSource(
            prepareCall = {
                applicationApiService.searchIllustration(
                    word = word,
                    sort = sort,
                    search_target = target,
                    bookmark_num_min = minBookmark,
                    bookmark_num_max = maxBookmark,
                    start_date = startDate,
                    end_date = endDate
                )
            },
            processResponse = {
                LoadResult.Successful(it.Illustrations, null, it.nextUrl)
            }
        )
    }

    fun searchNovel(
        word: String,
        sort: String,
        target: String,
        minBookmark: Long? = null,
        maxBookmark: Long? = null,
        startDate: String? = null,
        endDate: String? = null
    ) = newController(config) {
        client.nextUrlDataSource(
            prepareCall = {
                applicationApiService.searchNovel(
                    word = word,
                    sort = sort,
                    search_target = target,
                    bookmark_num_min = minBookmark,
                    bookmark_num_max = maxBookmark,
                    start_date = startDate,
                    end_date = endDate
                )
            },
            processResponse = {
                LoadResult.Successful(it.novels, null, it.nextUrl)
            }
        )
    }

    fun searchUser(
        word: String
    ) = newController(config) {
        client.nextUrlDataSource(
            prepareCall = {
                applicationApiService.searchUser(word = word)
            },
            processResponse = {
                LoadResult.Successful(it.userPreviews, null, it.nextUrl)
            }
        )
    }

    fun trendingTagsIllustration(
    ) = applicationApiService.trendingTagsIllustration()

    fun trendingTagsNovel(
    ) = applicationApiService.trendingTagsNovel()

    fun userFollowAdd(
        id: Long,
        restrict: Restrict = Restrict.PUBLIC
    ) = applicationApiService.userFollowAdd(
        user_id = id,
        restrict = restrict.toString()
    )

    fun userFollowDelete(
        id: Long
    ) = applicationApiService.userFollowDelete(
        user_id = id
    )

    fun userFollowDetail(
        id: Long
    ) = applicationApiService.userFollowDetail(
        user_id = id
    )

}

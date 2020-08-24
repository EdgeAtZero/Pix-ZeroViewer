package com.edgeatzero.projects.pixiv.model.util

import com.edgeatzero.library.model.ResponseState
import com.edgeatzero.library.util.HttpMessageConstant.message_data_is_empty
import com.edgeatzero.projects.pixiv.constant.Restrict
import com.edgeatzero.projects.pixiv.database.BrowsingHistoryEntity
import com.edgeatzero.projects.pixiv.event.RefreshEvent
import com.edgeatzero.projects.pixiv.http.repository.ApplicationRepository
import com.edgeatzero.projects.pixiv.http.repository.DatabaseRepository
import com.edgeatzero.projects.pixiv.http.suspendSource
import com.edgeatzero.projects.pixiv.model.Illustration
import com.edgeatzero.projects.pixiv.model.Novel
import com.edgeatzero.projects.pixiv.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import org.greenrobot.eventbus.EventBus
import retrofit2.Call

object ViewModelUtils {

    suspend fun ApplicationRepository.like(
        item: Illustration?,
        restrict: Restrict?,
        tag: List<String>?,
        after: ((Illustration) -> Unit)? = null
    ) = withContext(Dispatchers.Default) {
        item ?: return@withContext ResponseState.failed(message_data_is_empty)
        return@withContext illustrationBookmarkAdd(
            id = item.id,
            restrict = restrict ?: Restrict.PUBLIC,
            tags = tag
        ).process {
            item.isLiked = true
            after?.invoke(item)
        }
    }

    suspend fun ApplicationRepository.like(
        item: Novel?,
        restrict: Restrict?,
        tag: List<String>?,
        after: ((Novel) -> Unit)? = null
    ) = withContext(Dispatchers.Default) {
        item ?: return@withContext ResponseState.failed(message_data_is_empty)
        return@withContext novelBookmarkAdd(
            id = item.id,
            restrict = restrict ?: Restrict.PUBLIC,
            tags = tag
        ).process {
            item.isLiked = true
            after?.invoke(item)
        }
    }

    suspend fun ApplicationRepository.toggleLike(
        item: Illustration?
    ) = withContext(Dispatchers.Default) {
        item ?: kotlin.run {
            return@withContext ResponseState.failed(message_data_is_empty)
        }
        val call = if (item.isLiked) illustrationBookmarkDelete(id = item.id)
        else illustrationBookmarkAdd(id = item.id)
        return@withContext call.process {
            item.isLiked = !item.isLiked
            EventBus.getDefault().post(RefreshEvent(item))
        }
    }

    suspend fun ApplicationRepository.toggleLike(
        item: Novel?
    ) = withContext(Dispatchers.Default) {
        item ?: kotlin.run {
            return@withContext ResponseState.failed(message_data_is_empty)
        }
        val call = if (item.isLiked) novelBookmarkDelete(id = item.id)
        else novelBookmarkAdd(id = item.id)
        return@withContext call.process {
            item.isLiked = !item.isLiked
            EventBus.getDefault().post(RefreshEvent(item))
        }
    }

    suspend fun ApplicationRepository.toggleFollow(
        item: User?
    ) = withContext(Dispatchers.Default) {
        item ?: kotlin.run {
            return@withContext ResponseState.failed(message_data_is_empty)
        }
        val call = if (item.isFollowed) userFollowDelete(id = item.id)
        else userFollowAdd(id = item.id)
        return@withContext call.process {
            item.isFollowed = !item.isFollowed
            EventBus.getDefault().post(RefreshEvent(item))
        }
    }

    suspend fun DatabaseRepository.history(
        item: Illustration?
    ) = withContext(Dispatchers.IO) {
        item?.let { insertBrowsingHistory(BrowsingHistoryEntity(it)) }
    }

    suspend fun DatabaseRepository.history(
        item: Novel?
    ) = withContext(Dispatchers.IO) {
        item?.let { insertBrowsingHistory(BrowsingHistoryEntity(it)) }
    }

    suspend fun Call<ResponseBody>.process(onSuccessful: () -> Unit) = suspendSource(
        onSuccessful = {
            onSuccessful.invoke()
            return@suspendSource ResponseState.SUCCESSFUL
        }, onFailed = { message, body ->
            return@suspendSource ResponseState.failed(body?.errorMessage ?: message)
        }
    )

}

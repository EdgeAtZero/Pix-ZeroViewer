package com.edgeatzero.projects.pixiv.ui.like

import android.app.Application
import androidx.lifecycle.*
import com.edgeatzero.library.base.BaseViewModel
import com.edgeatzero.library.ext.toast
import com.edgeatzero.library.model.ResponseState
import com.edgeatzero.library.util.HttpMessageConstant
import com.edgeatzero.projects.pixiv.R
import com.edgeatzero.projects.pixiv.constant.ContentType
import com.edgeatzero.projects.pixiv.constant.Restrict
import com.edgeatzero.projects.pixiv.event.RefreshEvent
import com.edgeatzero.projects.pixiv.http.repository.AccountRepository
import com.edgeatzero.projects.pixiv.http.repository.ApplicationRepository
import com.edgeatzero.projects.pixiv.http.suspendExecute
import com.edgeatzero.projects.pixiv.ui.common.SelectData
import com.edgeatzero.projects.pixiv.util.Settings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.greenrobot.eventbus.EventBus
import org.kodein.di.generic.instance
import java.util.*

class LikeProViewModel(
    application: Application,
    handle: SavedStateHandle
) : BaseViewModel(application) {

    companion object {

        const val KEY_ID = "key_id"
        const val KEY_TYPE = "key_type"
        const val KEY_TAGS = "key_tags"
        const val KEY_LIKED = "key_liked"
        const val KEY_RESTRICT = "key_restrict"

    }

    private val accountRepository by instance<AccountRepository>()

    private val applicationRepository by instance<ApplicationRepository>()

    val account = accountRepository.currentAccountAsLiveData

    private val _id = handle.getLiveData<Long>(KEY_ID)

    private val _type = handle.getLiveData<ContentType>(KEY_TYPE)
    val type: LiveData<ContentType> = _type

    private val _tags = handle.getLiveData<LinkedList<SelectData<String>>>(KEY_TAGS)
    val tags: LiveData<out List<SelectData<String>>> = _tags

    private val _liked = handle.getLiveData<Boolean>(KEY_LIKED)
    val liked: LiveData<Boolean> = _liked

    private val _restrict = handle.getLiveData<Restrict>(KEY_RESTRICT, Restrict.PUBLIC)
    val isPrivate = _restrict.map { it == Restrict.PRIVATE }

    private val _likeState = MutableLiveData<ResponseState>()
    val likeState: LiveData<ResponseState> = _likeState

    private val _loadState = MutableLiveData<ResponseState>(ResponseState.NULL)
    val loadState: LiveData<ResponseState> = _loadState

    private val _text = MutableLiveData<String>()
    val text: LiveData<String> = _text

    val isUser = _type.map { it == ContentType.USER }

    private val _retry = MutableLiveData<() -> Unit>()
    val retry: LiveData<() -> Unit> = _retry

    fun postData(id: Long, type: ContentType?, tags: ArrayList<String>?) {
        type ?: return
        if (_id.value != null && _type.value != null && _tags.value != null) return
        _loadState.value = ResponseState.LOADING
        _id.value = id
        _type.value = type
        if (Settings.likeUseOriginalTags) {
            val list = tags?.mapIndexed { index, s ->
                SelectData(s, index < 10)
            }.let { LinkedList(it ?: emptyList()) }
            _tags.value = list
            _text.value = "${list.count { it.selected }} / ${list.size}"
            _loadState.value = ResponseState.SUCCESSFUL
        } else network(id, type)
    }

    fun toggleRestrict() {
        val b = _restrict.value == Restrict.PUBLIC
        _restrict.value = if (b) Restrict.PRIVATE else Restrict.PUBLIC
    }

    fun postToggle(data: SelectData<String>) {
        val tags = _tags.value ?: return
        if (tags.indexOf(data) == -1) return
        if (!data.selected && tags.count { it.selected } > 9) {
            val application = getApplication<Application>()
            application.toast { application.getString(R.string.message_can_only_ten_tag) }
        } else {
            data.toggle()
            _text.value = "${tags.count { it.selected }} / ${tags.size}"
        }
    }

    fun postTag(string: String): Boolean {
        val tags = _tags.value ?: return false
        return when {
            tags.any { it.data == string } -> {
                val application = getApplication<Application>()
                application.toast { application.getString(R.string.tips_cannot_be_added_repeatedly) }
                false
            }
            string.isBlank() -> {
                val application = getApplication<Application>()
                application.toast { application.getString(R.string.tips_cannot_be_empty) }
                false
            }
            tags.count { it.selected } > 9 -> {
                val application = getApplication<Application>()
                application.toast { application.getString(R.string.message_can_only_ten_tag) }
                false
            }
            else -> {
                tags.addFirst(SelectData(string, true))
                _text.value = "${tags.count { it.selected }} / ${tags.size}"
                true
            }
        }
    }

    fun swap(from: Int, to: Int): Boolean {
        val tags = _tags.value ?: return false
        Collections.swap(tags, from, to)
        _text.value = "${tags.count { it.selected }} / ${tags.size}"
        return true
    }

    fun deleteAt(index: Int): Boolean {
        val tags = _tags.value ?: return false
        tags.removeAt(index)
        _text.value = "${tags.count { it.selected }} / ${tags.size}"
        return true
    }

    private fun network(id: Long, type: ContentType) {
        viewModelScope.launch(Dispatchers.Default) {
            withContext(Dispatchers.Main) { _loadState.value = ResponseState.LOADING }
            if (type == ContentType.USER) applicationRepository.userFollowDetail(id).suspendExecute(
                onSuccessful = { body ->
                    withContext(Dispatchers.Main) {
                        _liked.value = body.followDetail.isFollowed
                        _restrict.value = body.followDetail.restrict
                        _loadState.value = ResponseState.SUCCESSFUL
                    }
                },
                onFailed = { message, body ->
                    withContext(Dispatchers.Main) {
                        _retry.value = { network(id, type) }
                        _loadState.value = ResponseState.failed(body?.errorMessage ?: message)
                    }
                }
            ) else when (type) {
                ContentType.NOVEL -> {
                    applicationRepository.novelBookmarkDetail(id)
                }
                ContentType.ILLUSTRATION, ContentType.MANGA -> {
                    applicationRepository.illustrationBookmarkDetail(id)
                }
                else -> null
            }?.suspendExecute(
                onSuccessful = { body ->
                    val tags = body.bookmarkDetail.tags.map {
                        SelectData(it.name, it.isRegistered)
                    }.let { LinkedList(it) }
                    withContext(Dispatchers.Main) {
                        _liked.value = body.bookmarkDetail.isBookmarked
                        _tags.value = tags
                        _text.value = "${tags.count { it.selected }} / ${tags.size}"
                        _restrict.value = body.bookmarkDetail.restrict
                        _loadState.value = ResponseState.SUCCESSFUL
                    }
                },
                onFailed = { message, body ->
                    withContext(Dispatchers.Main) {
                        _retry.value = { network(id, type) }
                        _loadState.value = ResponseState.failed(body?.errorMessage ?: message)
                    }
                }
            )
        }
    }

    var toLiked = false

    fun action(toLiked: Boolean) {
        viewModelScope.launch(Dispatchers.Default) {
            withContext(Dispatchers.Main) {
                _likeState.value = ResponseState.LOADING
                this@LikeProViewModel.toLiked = toLiked
            }
            val id = _id.value
            val type = _type.value
            val tags = _tags.value?.filter { it.selected }?.map { it.data }
            val restrict = _restrict.value
            if (id == null || type == null || restrict == null) {
                _likeState.value = ResponseState.failed(HttpMessageConstant.message_data_not_ready)
                return@launch
            }
            when (type) {
                ContentType.ILLUSTRATION, ContentType.MANGA -> if (toLiked) {
                    applicationRepository.illustrationBookmarkAdd(id, restrict, tags)
                } else {
                    applicationRepository.illustrationBookmarkDelete(id)
                }
                ContentType.NOVEL -> if (toLiked) {
                    applicationRepository.novelBookmarkAdd(id, restrict, tags)
                } else {
                    applicationRepository.novelBookmarkDelete(id)
                }
                ContentType.USER -> if (toLiked) {
                    applicationRepository.userFollowAdd(id, restrict)
                } else {
                    applicationRepository.userFollowDelete(id)
                }
            }.suspendExecute(
                onSuccessful = {
                    withContext(Dispatchers.Main) {
                        _likeState.value = ResponseState.SUCCESSFUL
                        EventBus.getDefault().post(RefreshEvent(type, id, toLiked))
                    }
                }, onFailed = { message, body ->
                    withContext(Dispatchers.Main) {
                        _likeState.value = ResponseState.failed(body?.errorMessage ?: message)
                    }
                }
            )
        }
    }
}

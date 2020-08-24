@file:Suppress("UNCHECKED_CAST")

package com.edgeatzero.projects.pixiv.ui.search

import android.app.Application
import androidx.lifecycle.*
import androidx.paging.PagedList
import com.edgeatzero.library.ext.notNullSwitchMap
import com.edgeatzero.library.ext.nullableSwitchMap
import com.edgeatzero.library.model.Controller
import com.edgeatzero.library.model.LoadState
import com.edgeatzero.library.model.ResponseState
import com.edgeatzero.projects.pixiv.constant.ContentType
import com.edgeatzero.projects.pixiv.constant.SearchSort
import com.edgeatzero.projects.pixiv.constant.SearchTarget
import com.edgeatzero.projects.pixiv.database.SearchHistoryEntity
import com.edgeatzero.projects.pixiv.http.repository.DatabaseRepository
import com.edgeatzero.projects.pixiv.http.suspendExecute
import com.edgeatzero.projects.pixiv.model.*
import com.edgeatzero.projects.pixiv.ui.common.BasicViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.kodein.di.generic.instance

class SearchViewModel(application: Application) : BasicViewModel(application) {

    private val databaseRepository by instance<DatabaseRepository>()

    private val searchText = MutableLiveData<String>()

    private val searchType = MutableLiveData<ContentType>(ContentType.ILLUSTRATION)

    private val _searched = MutableLiveData<Boolean>(false)
    val searched: LiveData<Boolean> = _searched

    private val _illustrations = MutableLiveData<Controller<Illustration>?>()
    val illustrations = _illustrations.convert()

    private val _novels = MutableLiveData<Controller<Novel>?>()
    val novels = _novels.convert()

    private val _user = MutableLiveData<Controller<UserPreview>?>()
    val user = _user.convert()

    val state: LiveData<LoadState>

    val retry: LiveData<(() -> Unit)?>

    val refresh: LiveData<(() -> Unit)?>

    private val _trendTags = MediatorLiveData<List<TrendTag>?>()
    val trendTags: LiveData<List<TrendTag>?> = _trendTags

    private val _loadState = MutableLiveData<ResponseState>()
    val loadState: LiveData<ResponseState> = _loadState

    private val _loadRetry = MutableLiveData<(() -> Unit)>()
    val loadRetry: LiveData<(() -> Unit)> = _loadRetry

    private val _histories = MediatorLiveData<List<SearchHistoryEntity>?>()
    val histories: LiveData<List<SearchHistoryEntity>?> = _histories

    private val _suggestions = MediatorLiveData<List<Tag>?>()
    val suggestions: LiveData<List<Tag>?> = _suggestions

    init {
        val controller = searchType.switchMap {
            when (it) {
                ContentType.NOVEL -> _novels as LiveData<Controller<*>?>
                ContentType.USER -> _user as LiveData<Controller<*>?>
                else -> _illustrations as LiveData<Controller<*>?>
            }
        }
        state = controller.notNullSwitchMap { it.state.invoke() }
        retry = controller.map { it?.retry }
        refresh = controller.map { it?.refresh }
        _trendTags.addSource(searchType, object : Observer<ContentType> {

            private var last: Boolean? = null

            override fun onChanged(t: ContentType?) {
                val b = t != ContentType.NOVEL
                if (last == b && t != null) return
                _loadState.value = ResponseState.LOADING
                _trendTags.value = null
                viewModelScope.launch(Dispatchers.Default) {
                    when (t) {
                        ContentType.NOVEL -> applicationRepository.trendingTagsNovel()
                        else -> applicationRepository.trendingTagsIllustration()
                    }.suspendExecute(
                        onSuccessful = { body ->
                            withContext(Dispatchers.Main) {
                                _trendTags.value = body.trendTags
                                _loadState.value = ResponseState.SUCCESSFUL
                            }
                        }, onFailed = { message, body ->
                            withContext(Dispatchers.Main) {
                                _loadRetry.value = { onChanged(t) }
                                _loadState.value =
                                    ResponseState.failed(body?.errorMessage ?: message)
                            }
                        }
                    )
                    last = b
                }
            }

        })
        _histories.addSource(searchText, object : Observer<String> {

            private var job: Job? = null

            init {
                onChanged(null)
            }

            override fun onChanged(t: String?) {
                job?.cancel()
                job = viewModelScope.launch(Dispatchers.Main) {
                    _histories.value = withContext(Dispatchers.IO) {
                        if (t.isNullOrBlank()) databaseRepository.querySearchHistory()
                        else databaseRepository.querySearchHistory("$t%")
                    }
                }
            }

        })
        _suggestions.addSource(searchText, object : Observer<String> {

            private var job: Job? = null

            override fun onChanged(t: String?) {
                job?.cancel()
                if (t.isNullOrBlank()) _suggestions.value = null
                else job = viewModelScope.launch(Dispatchers.Default) {
                    applicationRepository.searchAutocomplete(t).suspendExecute(
                        onSuccessful = { body ->
                            withContext(Dispatchers.Main) {
                                _suggestions.value = body.tags
                            }
                        },
                        onFailed = { _, _ ->
                            withContext(Dispatchers.Main) {
                                _suggestions.value = emptyList()
                            }
                        }
                    )
                }
            }

        })
    }

    fun postSearchText(text: String) {
        searchText.value = text
    }

    fun postSearchType(type: ContentType) {
        searchType.value = type
        if (searched.value == true) action()
    }

    fun clearHistories() {
        viewModelScope.launch(Dispatchers.IO) { databaseRepository.deleteHistory() }
        _histories.value = null
    }

    fun clearController() {
        _illustrations.value = null
        _novels.value = null
        _user.value = null
        _searched.value = false
    }

    fun action() {
        val searchText = searchText.value ?: return
        val searchType = searchType.value ?: return
        if (searchText.isBlank()) return
        _searched.value = true
        viewModelScope.launch(Dispatchers.Default) {
            databaseRepository.insertSearchHistory(SearchHistoryEntity(keyword = searchText))
            when (searchType) {
                ContentType.ILLUSTRATION, ContentType.MANGA -> {
                    applicationRepository.searchIllustration(
                        word = searchText,
                        sort = SearchSort.DATE_DESC.toString(),
                        target = SearchTarget.PARTIAL_MATCH_FOR_TAGS.toString()
                    ).let { withContext(Dispatchers.Main) { _illustrations.value = it } }
                    withContext(Dispatchers.Main) {
                        _novels.value = null
                        _user.value = null
                    }
                }
                ContentType.NOVEL -> {
                    applicationRepository.searchNovel(
                        word = searchText,
                        sort = SearchSort.DATE_DESC.toString(),
                        target = SearchTarget.PARTIAL_MATCH_FOR_TAGS.toString()
                    ).let { withContext(Dispatchers.Main) { _novels.value = it } }
                    withContext(Dispatchers.Main) {
                        _illustrations.value = null
                        _user.value = null
                    }
                }
                ContentType.USER -> {
                    applicationRepository.searchUser(
                        word = searchText
                    ).let { withContext(Dispatchers.Main) { _user.value = it } }
                    withContext(Dispatchers.Main) {
                        _illustrations.value = null
                        _novels.value = null
                    }
                }
            }
        }
    }

    private fun <T : Any> LiveData<Controller<T>?>.convert(): LiveData<PagedList<T>?> {
        return nullableSwitchMap { it.data }
    }

}

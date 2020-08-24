package com.edgeatzero.projects.pixiv.ui.illustration

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.edgeatzero.library.model.Controller
import com.edgeatzero.library.model.ResponseState
import com.edgeatzero.projects.pixiv.http.repository.DatabaseRepository
import com.edgeatzero.projects.pixiv.http.suspendExecute
import com.edgeatzero.projects.pixiv.model.Illustration
import com.edgeatzero.projects.pixiv.model.util.ViewModelUtils.history
import com.edgeatzero.projects.pixiv.ui.common.MultiPagingViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.kodein.di.generic.instance

class IllustrationDetailViewModel(
    application: Application,
    private val handle: SavedStateHandle
) : MultiPagingViewModel<Int, suspend () -> Controller<*>>(application) {

    companion object {

        const val KEY_IDS = "key_iods"

    }

    private val databaseRepository by instance<DatabaseRepository>()

    var cache: List<Illustration>? = null

    private val illustrations = ArrayList<Illustration>()

    private var ids
        get() = handle.get<List<Long>>(KEY_IDS)
        set(value) = handle.set(KEY_IDS, value)

    fun getIllustration(index: Int): Illustration? {
        return try {
            illustrations[index]
        } catch (e: IndexOutOfBoundsException) {
            null
        }
    }

    operator fun get(index: Int): IllustrationHolder? {
        val data = MutableLiveData<Illustration>()
        val state = MutableLiveData<ResponseState>()
        return try {
            data.value = illustrations[index]
            state.value = ResponseState.SUCCESSFUL
            IllustrationHolder(data, state)
        } catch (e: IndexOutOfBoundsException) {
            val id = try {
                ids?.get(index)
            } catch (e: IndexOutOfBoundsException) {
                null
            }
            if (id != null) {
                viewModelScope.launch(Dispatchers.Default) {
                    withContext(Dispatchers.Main) {
                        state.value = ResponseState.LOADING
                    }
                    applicationRepository.illustrationDetail(id).suspendExecute(
                        onSuccessful = { body ->
                            withContext(Dispatchers.Main) {
                                data.value = body.Illustration
                                illustrations[index] = body.Illustration
                                state.value = ResponseState.SUCCESSFUL
                            }
                        },
                        onFailed = { message, body ->
                            withContext(Dispatchers.Main) {
                                state.value = ResponseState.failed(body?.errorMessage ?: message)
                            }
                        }
                    )
                }
                IllustrationHolder(data, state)
            } else {
                return null
            }
        }
    }

    override suspend fun processInitializer(initializer: suspend () -> Controller<*>): Controller<*> {
        return initializer.invoke()
    }

    fun postIllustrations(illustrations: List<Illustration>?) {
        illustrations ?: return
        this.illustrations.addAll(illustrations)
        ids = illustrations.map { it.id }
    }

    fun insertHistoryAction(index: Int) {
        viewModelScope.launch(Dispatchers.Default) {
            databaseRepository.history(getIllustration(index))
        }
    }

    data class IllustrationHolder(
        val data: LiveData<Illustration>,
        val state: LiveData<ResponseState>
    )

}

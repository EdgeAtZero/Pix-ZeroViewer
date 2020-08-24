package com.edgeatzero.projects.pixiv.ui.intent

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.edgeatzero.library.base.BaseActivity
import com.edgeatzero.library.ext.startActivity
import com.edgeatzero.library.model.ResponseState
import com.edgeatzero.projects.pixiv.R
import com.edgeatzero.projects.pixiv.databinding.ActivityIntentProcessBinding
import com.edgeatzero.projects.pixiv.http.repository.ApplicationRepository
import com.edgeatzero.projects.pixiv.http.suspendExecute
import com.edgeatzero.projects.pixiv.model.Illustration
import com.edgeatzero.projects.pixiv.ui.common.DataChannel
import com.edgeatzero.projects.pixiv.ui.common.extras_uuid
import com.edgeatzero.projects.pixiv.ui.illustration.IllustrationDetailActivity
import com.edgeatzero.projects.pixiv.util.Settings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.kodein.di.generic.instance
import java.util.*

class IntentProcessActivity : BaseActivity<ActivityIntentProcessBinding>() {

    override val binding by binding(R.layout.activity_intent_process)

    private val applicationRepository by instance<ApplicationRepository>()

    private val _state = MutableLiveData<ResponseState>(ResponseState.NULL)
    val state: LiveData<ResponseState> = _state

    private val _retry = MutableLiveData<() -> Unit>()
    val retry: LiveData<() -> Unit> = _retry

    override fun onCreate(savedInstanceState: Bundle?) {
        delegate.localNightMode = Settings.nightMode
        super.onCreate(savedInstanceState)
        isLayoutFullscreen = true
        statusBarColor = Color.TRANSPARENT

        binding.activity = this

        processIntent(intent)
    }

    private fun processIntent(intent: Intent) {
        lifecycleScope.launch(Dispatchers.Default) {
            withContext(Dispatchers.Main) {
                _state.value = ResponseState.LOADING
            }
            val uri = intent.data ?: kotlin.run {
                withContext(Dispatchers.Main) {
                    _state.value = ResponseState.failed("No data", false)
                }
                return@launch
            }
            when (uri.scheme) {
                "pixiv" -> processPixivScheme(uri)
                "https" -> processWebScheme(uri)
                else -> unknownUri(uri)
            }
        }
    }

    private val number = "\\d+".toRegex()

    private suspend fun processPixivScheme(uri: Uri) {
        val path = uri.path
        when {
            uri.host == "illusts" -> processIllustrationId(path?.let { number.find(it)?.value?.toLongOrNull() })
            else -> unknownUri(uri)
        }
    }

    private suspend fun processWebScheme(uri: Uri) {
        val path = uri.path
        when {
            path?.startsWith("/artworks") == true -> {
                processIllustrationId(number.matchEntire(path)?.next()?.value?.toLongOrNull())
            }
            else -> unknownUri(uri)
        }
    }

    private suspend fun processIllustrationId(id: Long?) {
        id ?: kotlin.run {
            withContext(Dispatchers.Main) {
                _state.value = ResponseState.failed("Id is null", false)
            }
            return
        }
        applicationRepository.illustrationDetail(id).suspendExecute(
            onSuccessful = { body ->
                withContext(Dispatchers.Main) {
                    _state.value = ResponseState.SUCCESSFUL
                    startIllustrationDetail(body.Illustration)
                }
            }, onFailed = { message, body ->
                withContext(Dispatchers.Main) {
                    _state.value = ResponseState.failed(body?.errorMessage ?: message)
                    _retry.value = { processIntent(intent) }
                }
            }
        )
    }

    private suspend fun unknownUri(uri: Uri) = withContext(Dispatchers.Main) {
        _state.value = ResponseState.failed("unknown uri: $uri", false)
    }

    private fun startIllustrationDetail(illustration: Illustration) {
        startIllustrationDetail(listOf(illustration))
    }

    private fun startIllustrationDetail(illustrations: List<Illustration>) {
        val uuid = UUID.randomUUID().toString()
        startActivity<IllustrationDetailActivity> { bundleOf(extras_uuid to uuid) }
        DataChannel[uuid] = illustrations
        finishAfterTransition()
    }

}

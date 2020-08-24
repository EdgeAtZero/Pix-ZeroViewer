package com.edgeatzero.projects.pixiv.ui.login

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.edgeatzero.library.base.BaseViewModel
import com.edgeatzero.library.ext.setIfDifferent
import com.edgeatzero.library.model.ResponseState
import com.edgeatzero.projects.pixiv.R
import com.edgeatzero.projects.pixiv.database.AccountEntity
import com.edgeatzero.projects.pixiv.http.repository.AccountRepository
import com.edgeatzero.projects.pixiv.http.suspendExecute
import com.edgeatzero.projects.pixiv.util.Settings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.kodein.di.generic.instance

class LoginViewModel(
    application: Application,
    private val handle: SavedStateHandle
) : BaseViewModel(application) {

    companion object {

        const val KEY_PAGE = "key_page"
        const val KEY_NICKNAME = "key_nickname"
        const val KEY_USERNAME = "key_username"
        const val KEY_PASSWORD = "key_password"

        const val MESSAGE_NICK_TOO_LENGTH =
            "Nickname is too long(must not greater than 15 characters)"

    }

    private val accountRepository by instance<AccountRepository>()

    private val page = handle.getLiveData(KEY_PAGE, 0)

    private val _state = MutableLiveData<ResponseState>()
    val state: LiveData<ResponseState> = _state

    private val _account = MutableLiveData<AccountEntity>()
    val account: LiveData<AccountEntity> = _account

    private var nickname: String
        get() = handle.get(KEY_NICKNAME) ?: ""
        set(value) {
            handle.set(KEY_NICKNAME, value)
        }

    private val _nicknameError = MutableLiveData<EditTextStatus>()
    val nicknameError: LiveData<EditTextStatus> = _nicknameError

    private var username: String
        get() = handle.get(KEY_USERNAME) ?: ""
        set(value) {
            handle.set(KEY_USERNAME, value)
        }

    private val _usernameError = MutableLiveData<EditTextStatus>()
    val usernameError: LiveData<EditTextStatus> = _usernameError

    private var password: String
        get() = handle.get(KEY_PASSWORD) ?: ""
        set(value) {
            handle.set(KEY_PASSWORD, value)
        }

    private val _passwordError = MutableLiveData<EditTextStatus>()
    val passwordError: LiveData<EditTextStatus> = _passwordError

    fun postPage(page: Int) {
        this.page.setIfDifferent(page)
    }

    fun postNickname(nickname: String) {
        this.nickname = nickname
        _nicknameError.value = EditTextStatus(false)
    }

    fun postUsername(username: String) {
        this.username = username
        _usernameError.value = EditTextStatus(false)
    }

    fun postPassword(password: String) {
        this.password = password
        _passwordError.value = EditTextStatus(false)
    }

    private fun checkNickname(): Boolean = nickname.isNotBlank().also {
        _nicknameError.value = if (it) EditTextStatus(false)
        else EditTextStatus(true, R.string.tips_cannot_be_empty)
    }

    private fun checkUsername(): Boolean = username.isNotBlank().also {
        _usernameError.value = if (it) EditTextStatus(false)
        else EditTextStatus(true, R.string.tips_cannot_be_empty)
    }

    private fun checkPassword(): Boolean = password.isNotBlank().also {
        _passwordError.value = if (it) EditTextStatus(false)
        else EditTextStatus(true, R.string.tips_cannot_be_empty)
    }

    private suspend fun login(username: String, password: String) {
        accountRepository.login(username, password).suspendExecute(
            onSuccessful = { body ->
                withContext(Dispatchers.IO) {
                    AccountEntity(body, password).apply {
                        Settings.account = this
                        Settings.accountId = id
                        Settings.authorization = body.response.accessToken
                    }
                }.also { entity ->
                    withContext(Dispatchers.Main) {
                        if (page.value == 1) _account.value = entity
                        _state.value = ResponseState.SUCCESSFUL
                    }
                }

            }, onFailed = { message, body ->
                withContext(Dispatchers.Main) {
                    _state.value = ResponseState.failed(body?.errorMessage ?: message)
                }
            }
        )
    }

    fun action() {
        val page = page.value ?: return
        when (page) {
            0 -> if (checkUsername() && checkPassword()) viewModelScope.launch(Dispatchers.Default) {
                withContext(Dispatchers.Main) { _state.value = ResponseState.LOADING }
                login(username, password)
            }
            1 -> if (checkNickname()) viewModelScope.launch(Dispatchers.Default) {
                withContext(Dispatchers.Main) { _state.value = ResponseState.LOADING }
                if (nickname.length > 15) return@launch withContext(Dispatchers.Main) {
                    _state.value = ResponseState.failed(MESSAGE_NICK_TOO_LENGTH)
                }
                accountRepository.register(nickname).suspendExecute(
                    onSuccessful = { body ->
                        login(body.body.userAccount, body.body.password)
                    }, onFailed = { message, body ->
                        withContext(Dispatchers.Main) {
                            _state.value = ResponseState.failed(body?.errorMessage ?: message)
                        }
                    }
                )
            }
        }
    }

}

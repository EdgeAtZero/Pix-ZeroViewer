package com.edgeatzero.projects.pixiv.http.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.switchMap
import com.edgeatzero.library.base.BaseRepository
import com.edgeatzero.projects.pixiv.database.AccountDao
import com.edgeatzero.projects.pixiv.database.AccountEntity
import com.edgeatzero.projects.pixiv.http.service.PixivAccountApiService
import com.edgeatzero.projects.pixiv.http.service.PixivAuthorizationApiService
import com.edgeatzero.projects.pixiv.util.Settings
import org.kodein.di.generic.instance

class AccountRepository(application: Application) : BaseRepository(application) {

    private val accountDao by instance<AccountDao>()

    private val authorizationApiService by instance<PixivAuthorizationApiService>()

    private val accountApiService by instance<PixivAccountApiService>()

    val currentAccount: AccountEntity?
        get() = queryAccount(Settings.accountId)

    val currentAccountAsLiveData: LiveData<AccountEntity>
        get() = Settings.accountIdLD.switchMap { queryAccountAsLiveData(it) }

    fun queryAccountCount(
    ) = accountDao.queryCount()

    fun insertAccount(
        vararg entity: AccountEntity
    ) = accountDao.insert(*entity)

    fun queryAccount(
        id: Long
    ) = accountDao.query(id)

    fun queryAccountAsLiveData(
        id: Long
    ) = accountDao.queryAsLiveData(id)

    fun queryAllAccount(
    ) = accountDao.queryAll()

    fun queryAllAccountAsLiveData(
    ) = accountDao.queryAllAsLiveData()

    fun deleteAccount(
        id: Long
    ) = accountDao.delete(AccountEntity(id = id))

    fun login(
        username: String,
        password: String
    ) = authorizationApiService.getAuthToken(
        username = username,
        password = password
    )

    fun refresh(
        refreshToken: String,
        deviceToken: String
    ) = authorizationApiService.refreshAuthToken(
        refresh_token = refreshToken,
        device_token = deviceToken
    )

    fun register(
        username: String
    ) = accountApiService.provisionalAccountsCreate(
        user_name = username
    )

}

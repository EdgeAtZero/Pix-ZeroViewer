package com.edgeatzero.projects.pixiv.http.repository

import android.app.Application
import com.edgeatzero.library.base.BaseRepository
import com.edgeatzero.projects.pixiv.database.BrowsingHistoryDao
import com.edgeatzero.projects.pixiv.database.BrowsingHistoryEntity
import com.edgeatzero.projects.pixiv.database.SearchHistoryDao
import com.edgeatzero.projects.pixiv.database.SearchHistoryEntity
import org.kodein.di.generic.instance

class DatabaseRepository(application: Application) : BaseRepository(application) {

    private val browsingHistoryDao by instance<BrowsingHistoryDao>()

    private val searchHistoryDao by instance<SearchHistoryDao>()

    fun querySearchHistory(
    ) = searchHistoryDao.query()

    fun querySearchHistory(
        keyword: String
    ) = searchHistoryDao.query(keyword)

    fun insertBrowsingHistory(
        vararg entities: BrowsingHistoryEntity
    ) = browsingHistoryDao.insert(*entities)

    fun insertSearchHistory(
        vararg entities: SearchHistoryEntity
    ) = searchHistoryDao.insert(*entities)

    fun deleteHistory(
    ) = searchHistoryDao.delete()

}

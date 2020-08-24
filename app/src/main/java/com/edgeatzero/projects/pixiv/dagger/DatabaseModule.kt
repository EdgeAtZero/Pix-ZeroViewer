package com.edgeatzero.projects.pixiv.dagger

import android.annotation.SuppressLint
import androidx.arch.core.executor.ArchTaskExecutor
import androidx.room.Room
import com.edgeatzero.projects.pixiv.database.*
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

private const val DATABASE_MODULE_TAG = "DBModule"

@SuppressLint("RestrictedApi")
val databaseModule = Kodein.Module(DATABASE_MODULE_TAG) {

    bind<ZeroViewerDataBase>() with singleton {
        Room.databaseBuilder(instance(), ZeroViewerDataBase::class.java, "database")
                .setTransactionExecutor(ArchTaskExecutor.getIOThreadExecutor())
                .fallbackToDestructiveMigration()
                .build()
    }

    bind<AccountDao>() with singleton { instance<ZeroViewerDataBase>().getAccountDao() }

    bind<BrowsingHistoryDao>() with singleton { instance<ZeroViewerDataBase>().getBrowsingHistoryDao() }

    bind<SearchHistoryDao>() with singleton { instance<ZeroViewerDataBase>().getSearchHistoryDao() }

    bind<EmojiDao>() with singleton { instance<ZeroViewerDataBase>().getEmojiDao() }

}

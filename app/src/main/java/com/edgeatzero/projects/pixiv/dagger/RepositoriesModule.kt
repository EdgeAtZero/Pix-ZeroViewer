package com.edgeatzero.projects.pixiv.dagger

import com.edgeatzero.projects.pixiv.http.repository.AccountRepository
import com.edgeatzero.projects.pixiv.http.repository.ApplicationRepository
import com.edgeatzero.projects.pixiv.http.repository.DatabaseRepository
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

private const val REPOSITORY_MODULE_TAG = "repositoryModule"

val repositoryModule = Kodein.Module(REPOSITORY_MODULE_TAG) {

    bind<AccountRepository>() with singleton { AccountRepository(instance()) }

    bind<ApplicationRepository>() with singleton { ApplicationRepository(instance()) }

    bind<DatabaseRepository>() with singleton { DatabaseRepository(instance()) }

}

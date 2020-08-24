package com.edgeatzero.projects.pixiv.ui.main

import android.app.Application
import com.edgeatzero.library.base.BaseViewModel
import com.edgeatzero.projects.pixiv.http.repository.AccountRepository
import com.edgeatzero.projects.pixiv.util.Settings
import org.kodein.di.generic.instance

class MainViewModel(
    application: Application
) : BaseViewModel(application) {

    private val accountRepository by instance<AccountRepository>()

    val account = accountRepository.currentAccountAsLiveData

}

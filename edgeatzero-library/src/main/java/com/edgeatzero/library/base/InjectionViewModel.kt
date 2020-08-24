package com.edgeatzero.library.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.kcontext

abstract class InjectionViewModel(
    application: Application
) : AndroidViewModel(application), KodeinAware {

    override val kodeinContext by lazy { kcontext(this) }

    override val kodein: Kodein by lazy {
        requireNotNull((getApplication<Application>() as? KodeinAware)?.kodein)
    }

}

package com.edgeatzero.library.base

import android.app.Application
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.kcontext

abstract class InjectionRepository(protected val application: Application) : KodeinAware {

    override val kodeinContext by lazy { kcontext(this) }

    override val kodein: Kodein by lazy {
        requireNotNull((application as? KodeinAware)?.kodein)
    }

}

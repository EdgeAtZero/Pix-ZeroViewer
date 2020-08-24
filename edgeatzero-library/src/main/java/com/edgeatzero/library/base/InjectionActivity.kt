package com.edgeatzero.library.base

import androidx.appcompat.app.AppCompatActivity
import org.kodein.di.Copy
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.android.retainedKodein
import org.kodein.di.generic.kcontext

abstract class InjectionActivity : AppCompatActivity(), KodeinAware {

    protected val parentKodein by closestKodein()

    override val kodeinContext by lazy { kcontext(this) }

    override val kodein by retainedKodein {
        extend(parentKodein, copy = Copy.NonCached)
    }

}

package com.edgeatzero.library.base

import androidx.appcompat.app.AppCompatDialogFragment
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.kcontext

abstract class InjectionDialogFragment : AppCompatDialogFragment(), KodeinAware {

    protected val parentKodein by closestKodein()

    override val kodeinContext by lazy { kcontext(this) }

    override val kodein: Kodein by lazy {
        val activity = requireActivity()
        val aware = activity as? KodeinAware
        val context = activity.applicationContext as? KodeinAware
        requireNotNull(aware?.kodein ?: context?.kodein)
    }

}

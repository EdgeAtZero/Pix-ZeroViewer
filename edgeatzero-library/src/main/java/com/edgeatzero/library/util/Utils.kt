package com.edgeatzero.library.util

import android.content.Context
import org.kodein.di.Kodein

object Utils {

    lateinit var context: Context

    lateinit var kodein: Kodein

    fun initContext(context: Context) {
        this.context = context
    }

    fun initKodein(kodein: Kodein) {
        this.kodein = kodein
    }

}

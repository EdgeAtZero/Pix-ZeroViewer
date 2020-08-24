package com.edgeatzero.library.util

import com.google.gson.Gson
import org.kodein.di.generic.instance

object GsonHolder {

    val INSTANCE: Gson by Utils.kodein.instance()

}

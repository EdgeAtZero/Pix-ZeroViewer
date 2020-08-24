package com.edgeatzero.library.util

import java.util.*

object LocalUtils {

    fun parseLocale(locale: String?): Locale? = locale?.split("_".toRegex(), 3)?.let {
        try {
            Locale(it[0], it[1], it[2])
        } catch (e: NullPointerException) {
            null
        }
    }

}


package com.edgeatzero.projects.pixiv.model.util

import okhttp3.HttpUrl.Companion.toHttpUrl

object UrlUtils {

    fun getParam(url: String?, params: String): String? = url?.toHttpUrl()?.queryParameter(params)

    fun getParams(url: String?): Map<String, String>? = url?.toHttpUrl()?.let { httpUrl ->
        val map = HashMap<String, String>()
        for (key in httpUrl.queryParameterNames) {
            httpUrl.queryParameter(key)?.let { map.put(key, it) }
        }
        map
    }

}

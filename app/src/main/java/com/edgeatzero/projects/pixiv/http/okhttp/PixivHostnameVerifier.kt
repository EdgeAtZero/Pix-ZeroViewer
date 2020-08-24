package com.edgeatzero.projects.pixiv.http.okhttp

import com.edgeatzero.library.common.TagHolder
import com.edgeatzero.library.ext.logd
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLSession

class PixivHostnameVerifier : HostnameVerifier {

    companion object: TagHolder(PixivHostnameVerifier::class)

    override fun verify(p0: String?, p1: SSLSession?): Boolean {
        logd("host:  $p0")
        return true
    }

}

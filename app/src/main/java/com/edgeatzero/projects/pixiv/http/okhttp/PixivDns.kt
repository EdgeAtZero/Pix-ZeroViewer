package com.edgeatzero.projects.pixiv.http.okhttp

import okhttp3.Dns
import java.net.InetAddress

class PixivDns : Dns {

    private val address by lazy { HashMap<String, List<InetAddress>>() }

    override fun lookup(hostname: String): List<InetAddress> =
        address[hostname] ?: arrayOf(
            "210.140.131.208",
            "210.140.131.206",
            "210.140.131.203",
            "210.140.131.209",
            "111.230.37.44"
        ).map { InetAddress.getByName(it) }

}

package com.edgeatzero.projects.pixiv.http.okhttp

import com.edgeatzero.library.common.TagHolder
import com.edgeatzero.library.ext.logd
import java.net.InetAddress
import java.net.Socket
import javax.net.ssl.SSLSocket
import javax.net.ssl.SSLSocketFactory

class PixivSSLSocketFactory : SSLSocketFactory() {

    companion object : TagHolder(PixivSSLSocketFactory::class)

    override fun getDefaultCipherSuites(): Array<String> = arrayOf()

    override fun createSocket(p0: Socket?, p1: String?, p2: Int, p3: Boolean): Socket {
        if (p0 == null) throw NullPointerException()
        return (getDefault().createSocket(p0.inetAddress, p2) as SSLSocket).apply {
            enabledProtocols = supportedProtocols
        }.also {
            if (p3) p0.close()
            logd("Address: ${p0.inetAddress.hostAddress}, Protocol: ${it.session.protocol}, PeerHost: ${it.session.peerHost}, CipherSuite: ${it.session.cipherSuite}.")
        }
    }

    override fun createSocket(p0: String?, p1: Int): Socket? = null

    override fun createSocket(p0: String?, p1: Int, p2: InetAddress?, p3: Int): Socket? = null

    override fun createSocket(p0: InetAddress?, p1: Int): Socket? = null

    override fun createSocket(p0: InetAddress?, p1: Int, p2: InetAddress?, p3: Int): Socket? = null

    override fun getSupportedCipherSuites(): Array<String> = arrayOf()

}

package com.edgeatzero.projects.pixiv.http.okhttp

import com.edgeatzero.library.ext.fromJson
import com.edgeatzero.projects.pixiv.constant.ClientConstant.CLIENT_ID
import com.edgeatzero.projects.pixiv.constant.ClientConstant.CLIENT_SECRET
import com.edgeatzero.projects.pixiv.database.AccountEntity
import com.edgeatzero.projects.pixiv.model.AuthTokenData
import com.edgeatzero.projects.pixiv.util.Settings
import okhttp3.*
import java.io.IOException
import java.net.HttpURLConnection
import java.nio.charset.Charset
import java.nio.charset.UnsupportedCharsetException

class PixivAuthorizationInterceptor(private val client: OkHttpClient) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        var authorization = request.getAuthorization() ?: Settings.authorization
        if (authorization.isBlank()) synchronized(this) {
            val account = Settings.account ?: return chain.proceed(request)
            val response = client.newCall(buildNewAuthorizationRequest(account)).execute()
            if (!response.isSuccessful) return response
            try {
                authorization = response.cloneToString().fromJson<AuthTokenData>().process(account)
            } catch (e: Exception) {
                e.printStackTrace()
                return response
            }
        }
        val proceed = chain.proceed(request.setAuthorization(authorization))
        if (proceed.isAuthorizationExpired) {
            synchronized(this) {
                val account = Settings.account ?: return proceed
                val response = client.newCall(buildNewAuthorizationRequest(account)).execute()
                if (!response.isSuccessful) return response
                try {
                    authorization = response.cloneToString().fromJson<AuthTokenData>().process(account)
                } catch (e: Exception) {
                    e.printStackTrace()
                    return response
                }
            }
            return chain.proceed(request.setAuthorization(authorization))
        }
        return proceed
    }

    private fun AuthTokenData.process(account: AccountEntity): String {
        Settings.account = account.update(data = this)
        Settings.authorization = response.accessToken
        return response.accessToken
    }

    private fun Response.cloneToString(): String {
        val responseBody = body
        val source = responseBody?.source() ?: return ""
        try {
            source.request(Long.MAX_VALUE)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        var charset = Charset.forName("UTF-8")
        val contentType = responseBody.contentType()
        if (contentType != null) {
            try {
                charset = contentType.charset(charset)!!
            } catch (e: UnsupportedCharsetException) {
                e.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return source.buffer.clone().readString(charset)
    }

    private val Response.isAuthorizationExpired: Boolean
        get() {
            return code == HttpURLConnection.HTTP_BAD_REQUEST && cloneToString().contains("Error occurred at the OAuth process")
        }

    private fun Request.getAuthorization(): String? =
        header("Authorization").let { if (it == null || it.isBlank()) null else it }

    private fun Request.setAuthorization(authorization: String): Request =
        newBuilder().headers(
            headers.newBuilder()
                .removeAll("Authorization")
                .add("Authorization", authorization.fill())
                .build()
        ).build()

    private fun String.fill(): String =
        (if (startsWith("Bearer ")) this else "Bearer $this")

    private fun buildNewAuthorizationRequest(account: AccountEntity): Request =
        Request.Builder().url("https://oauth.secure.pixiv.net/auth/token").method(
            "POST", FormBody.Builder()
                .add("client_id", CLIENT_ID)
                .add("client_secret", CLIENT_SECRET)
                .add("grant_type", "refresh_token")
                .add("refresh_token", account.refresh_token)
                .add("device_token", account.device_token)
                .add("get_secure_url", "true")
                .add("include_policy", "true")
                .build()
        ).build()

}

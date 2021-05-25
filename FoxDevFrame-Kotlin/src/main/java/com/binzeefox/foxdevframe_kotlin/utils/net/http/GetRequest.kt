package com.binzeefox.foxdevframe_kotlin.utils.net.http

import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

/**
 * GET请求
 *
 * @author tong.xw
 * 2021/01/29 14:53
 */
class GetRequest(
        url: URL,
        connectTimeout: Int = 5000,
        readTimeout: Int = 5000
): ClientInterface {
    private val connection: HttpURLConnection =
            (url.openConnection() as HttpURLConnection).apply {
                requestMethod = "GET"
                this.connectTimeout = connectTimeout
                this.readTimeout = readTimeout
            }

    override fun request(onCallListener: ClientInterface.OnCallListener) {
        try {
            onCallListener.onStart(connection)
            connection.connect()
            val responseCode = connection.responseCode
            if (responseCode == 200) onCallListener.onSuccess(connection, responseCode, connection.inputStream)
            else onCallListener.onSuccess(connection, responseCode, null)
        } catch (e: Exception) {
            onCallListener.onError(e)
        } finally {
            onCallListener.onComplete()
        }
    }
}
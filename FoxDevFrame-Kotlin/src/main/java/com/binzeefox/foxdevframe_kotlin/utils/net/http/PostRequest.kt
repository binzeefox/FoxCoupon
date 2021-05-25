package com.binzeefox.foxdevframe_kotlin.utils.net.http

import com.binzeefox.foxdevframe_kotlin.utils.LogUtil
import java.io.BufferedWriter
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

/**
 * Post请求
 *
 * @param url 路径
 * @param body  请求体
 * @param charsetName   编码集
 * @author tong.xw
 * 2021/01/29 15:06
 */
class PostRequest(
        url: URL,
        connectTimeout: Int = 5000,
        readTimeout: Int = 5000,
        private val body: String,   //请求体
        private val charsetName: String = "utf-8", //编码集
        doInput: Boolean = true,
        doOutput: Boolean = true,
        useCaches: Boolean = false
) : ClientInterface {

    private val connection: HttpURLConnection =
            (url.openConnection() as HttpURLConnection).apply {
                requestMethod = "POST"
                this.connectTimeout = connectTimeout
                this.readTimeout = readTimeout

                this.doInput = doInput
                this.doOutput = doOutput
                this.useCaches = useCaches
            }

    override fun request(onCallListener: ClientInterface.OnCallListener) {
        try {
            BufferedWriter(OutputStreamWriter(connection.outputStream, charsetName)).use { writer ->
                onCallListener.onStart(connection)
                connection.connect()
                writer.write(body)
                val responseCode = connection.responseCode
                onCallListener.onSuccess(connection, responseCode, connection.inputStream)
            }
        } catch (e: Exception) {
            LogUtil("POST_REQUEST").setMessage("request: 请求失败").setThrowable(e).e()
            onCallListener.onError(e)
            connection.disconnect()
        } finally {
            onCallListener.onComplete()
        }
    }
}
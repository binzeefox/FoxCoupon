package com.binzeefox.foxdevframe_kotlin.utils.net.http

import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.URL

/**
 * 网络工具
 *
 * @author tong.xw
 * 2021/01/29 15:20
 */
object ClientUtil {

    // 基本路径
    @Volatile
    var baseUrl: String = ""
        set(value) {
            var v = value
            if (!v.endsWith("/")) v += "/"
            field = value
        }

    /**
     * 流转字符串
     */
    fun InputStream.toCharsetString(charsetName: String = "utf-8"): String? {
        try {
            BufferedReader(InputStreamReader(this, charsetName)).use { reader ->
                var line = ""
                val sb = StringBuilder()
                while (reader.readLine().also { line = it } != null)
                    sb.append(line)
                return sb.toString().trim()
            }
        } catch (e: Exception) {
            return null
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // 业务方法
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Get请求
     *
     * @param urlString api
     * @param params 参数
     */
    fun getMethod(urlString: String, params: Map<String, String> = emptyMap()): ClientInterface =
            GetRequest(convertGETUrl(urlString, params))

    /**
     * Post请求
     *
     * @param urlString api
     * @param body 请求体
     * @param charsetName 字符集
     */
    fun postMethod(urlString: String, body: String, charsetName: String): ClientInterface =
            PostRequest(
                    url = convertUrl(urlString),
                    body = body,
                    charsetName = charsetName
            )

    ///////////////////////////////////////////////////////////////////////////
    // 内部方法
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 创建URL
     */
    private fun convertUrl(urlString: String): URL {
        var temp = urlString
        if (temp.startsWith("/")) temp = temp.substring(1)
        return URL(baseUrl + urlString)
    }

    /**
     * 创建GET Url
     */
    private fun convertGETUrl(urlString: String, params: Map<String, String>): URL {
        if (params.isEmpty()) return convertUrl(urlString)
        var temp = urlString
        if (temp.endsWith("/")) temp = temp.substring(0, temp.length-1)
        temp = "$baseUrl$temp?"
        val sb = StringBuilder(temp)
        for ((key, value) in params)
            sb.append("${key}=$value&")
        temp = sb.substring(0, sb.length - 1)
        return URL(temp)
    }
}
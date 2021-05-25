package com.binzeefox.foxdevframe_kotlin.utils.net.http

import java.io.InputStream
import java.net.HttpURLConnection

/**
 * 网络工具接口
 *
 * @author tong.xw
 * 2021/01/29 14:47
 */
interface ClientInterface {

    /**
     * 开始请求
     */
    fun request(onCallListener: OnCallListener)

    ///////////////////////////////////////////////////////////////////////////
    // 内部类
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 请求回调
     */
    interface OnCallListener {

        /**
         * 开始前回调
         */
        fun onStart(connection: HttpURLConnection)

        /**
         * 请求成功
         *
         * @param connection   连接实例
         * @param responseCode 响应码
         * @param inputStream  输入流
         */
        fun onSuccess(connection: HttpURLConnection, responseCode: Int, inputStream: InputStream?)

        /**
         * 报错
         */
        fun onError(throwable: Throwable);

        /**
         * 结束
         */
        fun onComplete()
    }
}
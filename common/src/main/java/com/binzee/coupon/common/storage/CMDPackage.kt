package com.binzee.coupon.common.storage

import org.java_websocket.WebSocket


/**
 * 网络请求信息包
 *
 * @author tong.xw
 * 2021/05/25 11:06
 */
abstract class CMDPackage(val client: WebSocket)  {
    abstract fun response()
}
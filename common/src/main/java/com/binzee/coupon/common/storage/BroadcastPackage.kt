package com.binzee.coupon.common.storage

import org.java_websocket.server.WebSocketServer

/**
 * 广播
 *
 * @author tong.xw
 * 2021/05/25 14:40
 */
abstract class BroadcastPackage(
    val server: WebSocketServer
) {
    abstract fun broadcast()
}
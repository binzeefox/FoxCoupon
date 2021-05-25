package com.binzee.couponserver.server.broadcast

import com.binzee.coupon.common.storage.BroadcastPackage
import com.binzee.couponserver.server.Commands
import org.java_websocket.server.WebSocketServer

/**
 * 广播心跳
 *
 * @author tong.xw
 * 2021/05/25 14:48
 */
class BroadcastHeartBeat(
    server: WebSocketServer
) : BroadcastPackage(server) {

    override fun broadcast() {
        val heartBeat = mapOf(
            Pair("method", "heart-beat"),
            Pair("type", 0),
            Pair("reqCode", -1)
        )
        server.broadcast(Commands.gson.toJson(heartBeat))
    }
}
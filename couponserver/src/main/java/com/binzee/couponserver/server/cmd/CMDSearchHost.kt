package com.binzee.couponserver.server.cmd

import com.binzee.coupon.common.storage.CMDPackage
import com.binzee.couponserver.server.Commands
import org.java_websocket.WebSocket

/**
 * 收到确认主机信号
 *
 * @author tong.xw
 * 2021/05/25 11:53
 */
class CMDSearchHost(
    client: WebSocket,
    private val reqCode: Int
) : CMDPackage(client) {

    override fun response() {
        val map = mapOf(
            Pair("method", "search-host"),
            Pair("type", 1),
            Pair("reqCode", reqCode)
        )
        client.send(Commands.gson.toJson(map))
    }
}
package com.binzee.couponserver.server.error

import com.binzee.coupon.common.storage.CMDPackage
import com.binzee.couponserver.server.Commands
import org.java_websocket.WebSocket

/**
 * 无此指令
 *
 * @author tong.xw
 * 2021/05/25 14:24
 */
class ErrorCMD(
    client: WebSocket,
    private val method: String,
    private val errorMsg: String,
    private val reqCode: Int
) : CMDPackage(client) {

    override fun response() {
        val map = mapOf(
            Pair("method", method),
            Pair("type", 1),
            Pair("reqCode", reqCode),
            Pair("errorMsg", errorMsg)
        )
        client.send(Commands.gson.toJson(map))
    }
}
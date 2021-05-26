package com.binzee.coupon.common.storage

import com.google.gson.GsonBuilder
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
    reqCode: String?
) : CMDPackage(client, reqCode) {
    private val gson = GsonBuilder().enableComplexMapKeySerialization().create()

    override fun response() {
        val map = mapOf(
            Pair("method", method),
            Pair("type", 1),
            Pair("reqCode", reqCode),
            Pair("errorMsg", errorMsg)
        )
        client.send(gson.toJson(map))
    }
}
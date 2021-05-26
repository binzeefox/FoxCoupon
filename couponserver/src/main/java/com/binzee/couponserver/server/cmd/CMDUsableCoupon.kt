package com.binzee.couponserver.server.cmd

import com.binzee.coupon.common.CouponApplication.Companion.couponDB
import com.binzee.coupon.common.storage.CMDPackage
import com.binzee.couponserver.server.Commands
import com.binzeefox.foxdevframe_kotlin.FoxCore
import org.java_websocket.WebSocket

/**
 * 查询所有可用兑换券
 *
 * @author tong.xw
 * 2021/05/25 12:17
 */
class CMDUsableCoupon(
    client: WebSocket,
    reqCode: String
): CMDPackage(client, reqCode) {

    override fun response() {
        val map = mapOf(
            Pair("method", "usable-coupon"),
            Pair("type", 1),
            Pair("reqCode", reqCode),
            Pair("params", FoxCore.couponDB.couponDao().queryUsableCoupon(System.currentTimeMillis()))
        )
        client.send(Commands.gson.toJson(map))
    }
}
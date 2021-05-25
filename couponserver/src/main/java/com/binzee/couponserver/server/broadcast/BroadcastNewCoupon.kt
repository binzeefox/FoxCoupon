package com.binzee.couponserver.server.broadcast

import com.binzee.coupon.common.db.Coupon
import com.binzee.coupon.common.storage.BroadcastPackage
import com.binzee.couponserver.server.Commands
import org.java_websocket.server.WebSocketServer

/**
 * 新兑换券广播
 *
 * @author tong.xw
 * 2021/05/25 14:40
 */
class BroadcastNewCoupon(
    server: WebSocketServer,
    private val coupon: Coupon
) : BroadcastPackage(server){

    override fun broadcast() {
        val map = hashMapOf(
            Pair("method", "new-coupon"),
            Pair("type", 0),
            Pair("reqCode", -1),
            Pair("params", coupon)
        )
        server.broadcast(Commands.gson.toJson(map))
    }
}
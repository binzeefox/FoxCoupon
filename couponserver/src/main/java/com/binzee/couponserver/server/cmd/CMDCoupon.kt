package com.binzee.couponserver.server.cmd

import com.binzee.coupon.common.CouponApplication.Companion.couponDB
import com.binzee.coupon.common.db.Coupon
import com.binzee.coupon.common.storage.CMDPackage
import com.binzee.couponserver.server.Commands
import com.binzeefox.foxdevframe_kotlin.FoxCore
import org.java_websocket.WebSocket

/**
 * 查询特定uid兑换券
 *
 * @author tong.xw
 * 2021/05/25 14:11
 */
class CMDCoupon(
    client: WebSocket,
    reqCode: String,
    private val params: Map<String, String?>
) : CMDPackage(client, reqCode) {

    override fun response() {
        val uid = params["uid"]
        val map = hashMapOf(
            Pair("method", "coupon"),
            Pair("type", 1),
            Pair("reqCode", reqCode)
        )

        if (uid.isNullOrBlank()) {
            map["errorMsg"] = "参数错误，参数不能为空"
        } else {
            val coupon = FoxCore.couponDB.couponDao().queryByUid(uid)
            if (coupon == null)
                map["params"] = arrayListOf<Coupon>()
            else map["params"] = coupon
        }
        client.send(Commands.gson.toJson(map))
    }
}
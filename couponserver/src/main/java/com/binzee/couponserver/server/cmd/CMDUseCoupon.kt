package com.binzee.couponserver.server.cmd

import com.binzee.coupon.common.CouponApplication.Companion.couponDB
import com.binzee.coupon.common.db.Coupon
import com.binzee.coupon.common.storage.CMDPackage
import com.binzee.couponserver.server.Commands
import com.binzeefox.foxdevframe_kotlin.FoxCore
import org.java_websocket.WebSocket

/**
 * 使用兑换券信号
 *
 * @author tong.xw
 * 2021/05/25 14:22
 */
class CMDUseCoupon(
    client: WebSocket,
    private val reqCode: Int,
    private val params: Map<String, String?>
): CMDPackage(client) {

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
            val coupon: Coupon? = FoxCore.couponDB.couponDao().queryByUid(uid)
            when {
                coupon == null -> {
                    map["errorMsg"] = "未找到目标兑换券"
                }
                coupon.depleteTime != -1L -> {
                    // 已被使用
                    map["errorMsg"] = "未找到目标兑换券"
                }
                else -> {
                    // 开始使用
                    coupon.depleteTime = System.currentTimeMillis()
                    FoxCore.couponDB.couponDao().update(coupon)
                }
            }
        }
        client.send(Commands.gson.toJson(map))
    }
}
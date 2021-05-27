package com.binzee.coupon.client.requests

import com.binzee.coupon.client.Commands

/**
 * 请求兑换券信息
 *
 * @author tong.xw
 * 2021/05/27 09:59
 */
class RequestCoupon (
    reqCode: String,
    private val couponUid: String
): Commands.RequestData("coupon", reqCode){

    override fun call() {
        val params = mapOf(
            Pair("uid", couponUid)
        )
        msgMap["params"] = params
        Commands.client.send(Commands.gson.toJson(msgMap))
    }
}
package com.binzee.coupon.client.requests

import com.binzee.coupon.client.Commands

/**
 * 请求可用兑换券
 *
 * @author tong.xw
 * 2021/05/27 09:51
 */
class RequestUsableCoupon(
    reqCode: String
): Commands.RequestData("usable-coupon", reqCode) {

    override fun call() {
        Commands.client.send(Commands.gson.toJson(msgMap))
    }

//    fun resSolveMsg(msg: String)
}
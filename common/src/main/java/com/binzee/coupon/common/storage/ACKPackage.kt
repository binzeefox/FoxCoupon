package com.binzee.coupon.common.storage

import com.google.gson.Gson

/**
 * 网络回复信息包
 *
 * @author tong.xw
 * 2021/05/25 11:09
 */
abstract class ACKPackage {

    abstract fun process()
}
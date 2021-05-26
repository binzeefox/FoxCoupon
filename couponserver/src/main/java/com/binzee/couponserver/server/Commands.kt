package com.binzee.couponserver.server

import android.util.Log
import com.binzee.couponserver.server.cmd.CMDCoupon
import com.binzee.couponserver.server.cmd.CMDSearchHost
import com.binzee.couponserver.server.cmd.CMDUsableCoupon
import com.binzee.couponserver.server.cmd.CMDUseCoupon
import com.binzee.coupon.common.storage.ErrorCMD
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.java_websocket.WebSocket
import org.json.JSONObject
import java.lang.Exception

/**
 * 交互任务
 *
 * @author tong.xw
 * 2021/05/25 11:31
 */
object Commands {
    private const val TAG = "Commands"
    val gson = GsonBuilder().enableComplexMapKeySerialization().create()
    val mapType = object : TypeToken<Map<String, String>>(){}.type

    /**
     * 收到信息
     */
    fun receive(msg: String?, conn: WebSocket?) {
        if (msg == null) return
        val data = JSONObject(msg)

        try {
            val type: Int = data.getInt("type")

            if (type == 0) processCMD(data, conn)
            else if (type == 1) processACK(data)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 处理请求
     */
    private fun processCMD(data: JSONObject, conn: WebSocket?) {
        val method: String = data.getString("method")
        val reqCode: String? = data.getString("reqCode")
        val params: String? = data.getString("params")

        if (reqCode == null) {
            Log.e(TAG, "processCMD: processCMD failed, null reqCode")
            ErrorCMD(
                conn!!,
                method,
                "请求码为空",
                reqCode
            ).response()
            return
        }

        try {
            // 挑选合适的CMDPackage实现，然后返回响应
            when(method) {
                // 搜索主机
                "search-host" -> CMDSearchHost(conn!!, reqCode!!)
                // 查询可用兑换券
                "usable-coupon" -> CMDUsableCoupon(conn!!, reqCode!!)
                // 查询特定uid兑换券
                "coupon" -> CMDCoupon(conn!!, reqCode!!, gson.fromJson(params, mapType))
                // 使用兑换券
                "use-coupon" -> CMDUseCoupon(conn!!, reqCode!!, gson.fromJson(params, mapType))
                else -> ErrorCMD(
                    conn!!,
                    method,
                    "无效操作",
                    reqCode!!
                )
            }.response()
        } catch (e: Exception) {
            Log.e(TAG, "processCMD: processCMD failed", e)
            ErrorCMD(
                conn!!,
                method,
                "服务器未知错误",
                reqCode
            ).response()
        }
    }

    /**
     * 处理回复
     */
    private fun processACK(data: JSONObject) {
        //TODO 挑选合适的实现，然后返回
        Log.d(TAG, "processACK: $data")
    }
}
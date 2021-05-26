package com.binzee.coupon.client

import android.util.Log
import com.binzee.coupon.common.storage.ErrorCMD
import com.binzeefox.foxdevframe_kotlin.utils.net.LanScanner
import org.java_websocket.WebSocket
import org.json.JSONObject
import java.lang.Exception
import java.net.URI

/**
 * 客户端命令
 *
 * TODO 暂定固定IP
 * @author tong.xw
 * 2021/05/26 09:57
 */
object Commands {
    private const val TAG = "Commands"

    fun receive(message: String) {
        val data = JSONObject(message)

        try {
            val type: Int = data.getInt("type")

            if (type == 0) processCMD(data)
            else if (type == 1) processACK(data)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 处理请求
     */
    private fun processCMD(data: JSONObject) {
        val method: String = data.getString("method")
        val reqCode: String? = data.getString("reqCode")
        val params: String? = data.getString("params")

        try {
            // 挑选合适的CMDPackage实现，然后返回响应

        } catch (e: Exception) {
            Log.e(TAG, "processCMD: processCMD failed", e)
            ErrorCMD(CouponSocketClient.instance, method, "客户端未知错误", reqCode)
        }
    }

    /**
     * 处理回复
     */
    private fun processACK(data: JSONObject) {
        val method: String = data.getString("method")
        val reqCode: String? = data.getString("reqCode")
        val params: String? = data.getString("params")

        try {
            // 挑选合适的CMDPackage实现，然后返回响应
            when (method) {

            }
        } catch (e: Exception) {
            Log.e(TAG, "processCMD: processCMD failed", e)
            ErrorCMD(CouponSocketClient.instance, method, "客户端未知错误", reqCode)
        }
    }
}
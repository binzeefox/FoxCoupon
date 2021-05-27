package com.binzee.coupon.client

import android.util.Log
import com.binzee.coupon.common.storage.ErrorCMD
import com.binzeefox.foxdevframe_kotlin.utils.net.LanScanner
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.java_websocket.WebSocket
import org.json.JSONObject
import java.lang.Exception
import java.net.URI
import java.util.*
import kotlin.collections.HashMap

/**
 * 客户端命令
 *
 * TODO 暂定固定IP
 * @author tong.xw
 * 2021/05/26 09:57
 */
object Commands {
    private const val TAG = "Commands"
    private val callbackArray = Vector<OnResponseListener>()
    val client: CouponSocketClient get() = CouponSocketClient.instance
    val gson = GsonBuilder().enableComplexMapKeySerialization().create()
    val mapType = object : TypeToken<Map<String, String>>(){}.type

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
     * 添加客户端命令
     */
    fun addResponseListener(listener: OnResponseListener) {
        callbackArray.add(listener)
    }

    /**
     * 移除监听器
     */
    fun removeResponseListener(listener: OnResponseListener) {
        callbackArray.remove(listener)
    }

    /**
     * 请求
     */
    fun request(data: RequestData) {
        data.call()
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
        val errorMsg: String? = data.getString("errorMsg")

        for (listener in callbackArray)
            listener.onResponse(reqCode, ResponseData(method, reqCode, params, errorMsg))
    }

    ///////////////////////////////////////////////////////////////////////////
    // 内部类
    ///////////////////////////////////////////////////////////////////////////

    data class ResponseData(
        val method: String,     // 方法名
        val reqCode: String?,    // 请求码
        val paramsJson: String?, // 参数
        val errorMsg: String?
    )

    abstract class RequestData(
        method: String,
        reqCode: String
    ) {
        val msgMap: HashMap<String, Any> = hashMapOf(
            Pair("method", method),
            Pair("type", 0),
            Pair(reqCode, reqCode)
        )
        abstract fun call()
    }

    interface OnResponseListener {
        fun onResponse(reqCode: String?, data: ResponseData)
    }
}
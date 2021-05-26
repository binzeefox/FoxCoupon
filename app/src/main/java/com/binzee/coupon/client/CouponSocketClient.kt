package com.binzee.coupon.client

import android.util.Log
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.lang.Exception
import java.net.URI

/**
 * 优惠券客户端
 *
 * @author tong.xw
 * 2021/05/26 09:57
 */
class CouponSocketClient(uri: URI): WebSocketClient(uri) {
    companion object {
        private const val TAG = "CouponSocketClient"
        val callbackArray = ArrayList<SocketClientCallback>()

        val instance : CouponSocketClient by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            CouponSocketClient(URI("ws://192.168.43.1:60001"))
        }

        interface SocketClientCallback {
            fun onOpen(handshakedata: ServerHandshake?){}
            fun onClose(code: Int, reason: String?, remote: Boolean){}
            fun onMessage(message: String?){}
            fun onError(ex: Exception?){}
        }
    }

    override fun onOpen(handshakedata: ServerHandshake?) {
        Log.d(TAG, "onOpen: $handshakedata")
        for (callback in callbackArray)
            callback.onOpen(handshakedata)
    }

    override fun onClose(code: Int, reason: String?, remote: Boolean) {
        Log.d(TAG, "onClose: code $code, reason $reason, remote $remote")
        for (callback in callbackArray)
            callback.onClose(code, reason, remote)
    }

    override fun onMessage(message: String?) {
        Log.d(TAG, "onMessage: $message")
        if (message == null) return
        Commands.receive(message)
        for (callback in callbackArray)
            callback.onMessage(message)
    }

    override fun onError(ex: Exception?) {
        Log.e(TAG, "onError: ", ex)
        for (callback in callbackArray)
            callback.onError(ex)
    }
}
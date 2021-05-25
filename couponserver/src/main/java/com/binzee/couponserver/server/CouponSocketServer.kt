package com.binzee.couponserver.server

import android.util.Log
import android.widget.Toast
import com.binzee.coupon.common.db.Coupon
import com.binzee.couponserver.server.broadcast.BroadcastHeartBeat
import com.binzee.couponserver.server.broadcast.BroadcastNewCoupon
import com.binzeefox.foxdevframe_kotlin.ui.utils.NoticeUtil
import org.java_websocket.WebSocket
import org.java_websocket.handshake.ClientHandshake
import org.java_websocket.server.WebSocketServer
import java.lang.Exception
import java.net.InetSocketAddress
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * 服务器
 *
 * @author tong.xw
 * 2021/05/25 11:17
 */
class CouponSocketServer : WebSocketServer(InetSocketAddress(60001)) {
    companion object {
        val instance: CouponSocketServer by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            CouponSocketServer()
        }
    }

    // 用于心跳的独立线程
    private var heartBeatExecutor: ExecutorService = Executors.newSingleThreadExecutor()
        get() {
            if (field.isShutdown) field = Executors.newSingleThreadExecutor()
            return field
        }

    init {
        isReuseAddr = true
    }

    /**
     * 广播新兑换券
     */
    fun notifyNewCoupon(coupon: Coupon) {
        BroadcastNewCoupon(this, coupon).broadcast()
    }

    /**
     * 广播心跳
     */
    private fun notifyHeartBeat() {
        BroadcastHeartBeat(this).broadcast()
    }

    override fun onOpen(conn: WebSocket?, handshake: ClientHandshake?) {
        Log.d("COUPON SERVER", "onOpen: ${conn?.remoteSocketAddress}")
    }

    override fun onClose(conn: WebSocket?, code: Int, reason: String?, remote: Boolean) {
        NoticeUtil.toast("服务关闭").setDuration(Toast.LENGTH_LONG).showNow()
        heartBeatExecutor.shutdownNow()
    }

    override fun onMessage(conn: WebSocket?, message: String?) = Commands.receive(message, conn)

    override fun onStart() {
        NoticeUtil.toast("服务开启").setDuration(Toast.LENGTH_LONG).showNow()
        heartBeatExecutor.execute {
            try {
                while (true) {
                    notifyHeartBeat()
                    Thread.sleep(1500)  // 延迟15秒
                }
            } catch (e: Exception) {
                Log.e("COUPON SERVER", "onOpen: loop end", e)
            }
        }
    }

    override fun onError(conn: WebSocket?, ex: Exception?) {
        NoticeUtil.toast("客户端连接出错").setDuration(Toast.LENGTH_LONG).showNow()
        Log.e("COUPON SERVER", "onError: ${conn?.remoteSocketAddress}", ex)
    }
}
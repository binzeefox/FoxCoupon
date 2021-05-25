package com.binzee.couponserver

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.binzee.couponserver.server.CouponSocketServer

/**
 * 兑换券后台服务
 *
 * @author tong.xw
 * 2021/05/25 14:54
 */
class CouponService: Service() {
    private val server = CouponSocketServer.instance

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int = START_STICKY
    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        server.start()
    }
}
package com.binzeefox.foxdevframe_kotlin.utils.net

import com.binzeefox.foxdevframe_kotlin.utils.LogUtil
import com.binzeefox.foxdevframe_kotlin.utils.device.DeviceStatus
import java.net.InetAddress
import java.util.concurrent.Executors

/**
 * 局域网扫描器
 *
 * @param myAddress 本地ip，默认从工具获取
 * @author tong.xw
 * 2021/01/29 14:17
 */
class LanScanner(
        private val myAddress: String = DeviceStatus.localIPAddress,
) {
    private val workThreadPool = Executors.newCachedThreadPool()    // 工作线程池
    private val loopThreadPool = Executors.newSingleThreadExecutor()    // 循环线程池

    // 回收掉
    private var isRecycled: Boolean = false
        set(value) {
            if (!value) {
                workThreadPool.shutdown()
                loopThreadPool.shutdown()
                field = value
            } else {
                LogUtil("LanScanner").setMessage("setRecycled: 操作失败，已经回收").w()
                return
            }
        }

    /**
     * 开始一轮扫描
     *
     * @param onResponseListener 结果回调，默认无操作
     */
    fun scan(onResponseListener: (InetAddress) -> Unit = {}) {
        val addressPre = myAddress.substring(0, myAddress.lastIndexOf("."))
        LogUtil("LanScanner").setMessage("scan: $addressPre").v()

        var i = 1
        while (i <= 255 && !isRecycled) {
            val ip = "${addressPre}.$i"
            if (ip != myAddress) pingIp(ip, onResponseListener)
            i++
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // 内部方法
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 检查IP是否可用
     */
    private fun pingIp(ip: String, listener: (InetAddress) -> Unit = {}) {
        workThreadPool.execute {
            val address = InetAddress.getByName(ip)
            val reachable = address.isReachable(3000)
            if (reachable) listener(address)
            logger(ip, reachable)
        }
    }

    /**
     * 日志
     */
    @Synchronized
    private fun logger(ip: String, reachable: Boolean){
        val tag = "LanScanner-Logger"

        LogUtil(tag).setMessage("pingIp: ----------------------------------------").v()
        LogUtil(tag).setMessage("pingIp: ip = $ip").v()
        LogUtil(tag).setMessage("pingIp: result = $reachable").v()
        LogUtil(tag).setMessage("pingIp: ----------------------------------------").v()
    }
}
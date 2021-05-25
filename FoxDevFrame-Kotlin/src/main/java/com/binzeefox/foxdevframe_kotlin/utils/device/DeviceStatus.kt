package com.binzeefox.foxdevframe_kotlin.utils.device

import android.Manifest
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.wifi.WifiManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import com.binzeefox.foxdevframe_kotlin.FoxCore
import java.net.Inet4Address
import java.net.NetworkInterface

/**
 * 手机状态
 *
 * @author tong.xw
 * 2021/01/27 17:09
 */
object DeviceStatus {

    //网络连接管理器
    private val connectivityManager: ConnectivityManager?
        get() = FoxCore.appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?

    ///////////////////////////////////////////////////////////////////////////
    // 网络相关
    ///////////////////////////////////////////////////////////////////////////

    // 网络连接状态
    val networkType: NetworkType
        @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
        get() {
            connectivityManager ?: return NetworkType.NONE  // 无网络
            val network = connectivityManager?.activeNetwork
            network ?: return NetworkType.NONE  // 无网络
            val capabilities = connectivityManager?.getNetworkCapabilities(network)
            capabilities ?: return NetworkType.NONE // 无网络
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ->
                    NetworkType.DATA
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ->
                    NetworkType.WIFI
                else -> NetworkType.NONE
            }
        }


    /**
     * 注册网络监听器
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    fun registerNetworkListener(callback: ConnectivityManager.NetworkCallback) {
        connectivityManager?.registerDefaultNetworkCallback(callback)
    }

    /**
     * 注销网络监听
     */
    fun unregisterNetworkListener(callback: ConnectivityManager.NetworkCallback) {
        connectivityManager?.unregisterNetworkCallback(callback)
    }

    // 内网IP
    val localIPAddress: String
        get() {
            val en = NetworkInterface.getNetworkInterfaces()
            while (en.hasMoreElements()) {
                val intf = en.nextElement()
                val enumIpAddr = intf.inetAddresses
                while (enumIpAddr.hasMoreElements()) {
                    val inetAddress = enumIpAddr.nextElement()
                    if (!inetAddress.isLoopbackAddress && inetAddress is Inet4Address) {
                        return inetAddress.getHostAddress().toString()
                    }
                }
            }
            return ""
        }

    // IP地址
    val ipAddress: String
        get() {
            return when (networkType) {
                NetworkType.NONE -> ""
                NetworkType.DATA -> getDataIPAddress()
                NetworkType.WIFI -> getWifiIPAddress()
            }
        }

    ///////////////////////////////////////////////////////////////////////////
    // 内部方法
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 获取移动网络的IP
     */
    private fun getDataIPAddress(): String {
        val en = NetworkInterface.getNetworkInterfaces()
        while (en.hasMoreElements()) {
            val intf = en.nextElement()
            val enumIpAddr = intf.inetAddresses
            while (enumIpAddr.hasMoreElements()) {
                val inetAddress = enumIpAddr.nextElement()
                if (!inetAddress.isLoopbackAddress) {
                    return inetAddress.hostAddress
                }
            }
        }
        return ""
    }

    /**
     * 获取WIFI IP地址
     */
    private fun getWifiIPAddress(): String {
        val info = (FoxCore.appContext.applicationContext
                .getSystemService(Context.WIFI_SERVICE) as WifiManager).connectionInfo
        val ip = info.ipAddress
        return intToIp(ip)
    }

    /**
     * ip转字符
     *
     */
    private fun intToIp(intIp: Int): String {
        return (intIp shr 24).toString() + "." +
                (intIp and 0x00FFFFFF shr 16) + "." +
                (intIp and 0x0000FFFF shr 8) + "." +
                (intIp and 0x000000FF)
    }
}

enum class NetworkType(val level: Int) {
    NONE(0),
    DATA(1),

    //    NR5G(2),
    WIFI(3)
}
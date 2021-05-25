package com.binzeefox.foxdevframe_kotlin.utils.device.bluetooth

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.annotation.RequiresPermission
import com.binzeefox.foxdevframe_kotlin.FoxCore

/**
 * 低功耗蓝牙工具
 *
 * @author tong.xw
 * 2021/01/28 15:28
 */
object BleUtils {
    abstract class BleScanCallback: ScanCallback() {
        fun onStop(){}
    }

    // 蓝牙管理器
    val manager: BluetoothManager
        get() =
            FoxCore.appContext.applicationContext.getSystemService(Context.BLUETOOTH_SERVICE)
                    as BluetoothManager

    // 蓝牙适配器
    val adapter: BluetoothAdapter get() = manager.adapter

    // 是否支持低功耗蓝牙
    val isSupportBle: Boolean get() = FoxCore.packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)

    // 是否开启蓝牙
    val isEnableBle: Boolean
        @RequiresPermission(Manifest.permission.BLUETOOTH)
        get() = adapter.isEnabled

    /**
     * 开启蓝牙
     */
    fun openBle() {
        Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE).let {
            var ctx: Context? = FoxCore.simulatedBackStack.peek()
            ctx?: run{
                ctx = FoxCore.appContext
                it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            ctx?.startActivity(it)
        }
    }

    /**
     * 获取扫描器
     */
    fun getScanner(callback: ScanCallback): BleScanner = BleScanner(callback)

    /**
     * 将系统封装改为Ble封装
     */
    fun getBleDevice(device: BluetoothDevice): BleDevice = BleDevice(device)

    /**
     * 根据物理地址获取设备
     */
    fun getBleDeviceByMac(mac: String): BleDevice = getBleDevice(adapter.getRemoteDevice(mac))
}
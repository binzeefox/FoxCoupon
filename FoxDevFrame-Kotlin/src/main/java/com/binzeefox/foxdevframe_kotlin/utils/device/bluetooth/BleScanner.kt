package com.binzeefox.foxdevframe_kotlin.utils.device.bluetooth

import android.Manifest
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanSettings
import android.os.Handler
import android.os.Looper
import androidx.annotation.RequiresPermission

/**
 * 低功耗蓝牙扫描器
 *
 * @author tong.xw
 * 2021/01/28 15:14
 */
class BleScanner(
        private val callback: ScanCallback,
        var timeout: Long = 0,
        var settings: ScanSettings? = null,
        val filterList: List<ScanFilter> = ArrayList()
) {

    /**
     * 开始扫描
     */
    @RequiresPermission(Manifest.permission.BLUETOOTH_ADMIN)
    fun beginScan() {
        BleUtils.adapter.bluetoothLeScanner.startScan(filterList, settings, callback)
        if (timeout != 0L) Handler(Looper.getMainLooper()).postDelayed(this::stopScan, timeout)
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_ADMIN)
    fun stopScan() {
        if (callback is BleUtils.BleScanCallback) callback.onStop()
        BleUtils.adapter.bluetoothLeScanner.stopScan(callback)
    }
}
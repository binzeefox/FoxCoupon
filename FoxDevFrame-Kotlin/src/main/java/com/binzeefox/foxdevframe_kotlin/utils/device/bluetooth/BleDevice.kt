package com.binzeefox.foxdevframe_kotlin.utils.device.bluetooth

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.os.Build
import android.os.Handler
import com.binzeefox.foxdevframe_kotlin.FoxCore
import java.util.*

/**
 * 低功耗蓝牙设备
 *
 * @author tong.xw
 * 2021/01/28 11:42
 */

@Suppress("MemberVisibilityCanBePrivate")
open class BleDevice(
        private val device: BluetoothDevice,
        private var autoConnect: Boolean = false,
        private var connectHandler: Handler? = null
) {
    private val ID_CCCD: UUID? = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")

    // 回调实例
    private var gattCallback: BleGattCallbackImpl? = null
    // GATT
    private val gatt: BluetoothGatt?
        get() = gattCallback?.gatt

    // 是否连接
    val isConnected: Boolean
        get() {
            gattCallback?.also {
                return it.isConnected
            }
            return false
        }

    /**
     * 连接
     */
    fun connect(callback: BleConnectCallback) {
        gattCallback = BleGattCallbackImpl(callback)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && connectHandler != null) {
            device.connectGatt(FoxCore.appContext, autoConnect, gattCallback, BluetoothDevice.TRANSPORT_AUTO, BluetoothDevice.PHY_LE_1M_MASK, connectHandler)
        } else device.connectGatt(FoxCore.appContext, autoConnect, gattCallback, BluetoothDevice.TRANSPORT_AUTO)
    }

    /**
     * 断开连接
     */
    fun disconnect() = gattCallback?.gatt?.disconnect()

    /**
     * 回收
     */
    fun recycle() {
        disconnect()
        gattCallback?.gatt?.close()
    }

    /**
     * 读特征
     */
    fun readCharacteristic(characteristic: BluetoothGattCharacteristic) =
            gatt?.readCharacteristic(characteristic)

    /**
     * 写特征
     */
    fun writeCharacteristic(characteristic: BluetoothGattCharacteristic) =
            gatt?.writeCharacteristic(characteristic)

    /**
     * 读描述
     */
    fun readDescriptor(descriptor: BluetoothGattDescriptor) =
            gatt?.readDescriptor(descriptor)

    /**
     * 写描述
     */
    fun writeDescriptor(descriptor: BluetoothGattDescriptor) =
            gatt?.writeDescriptor(descriptor)

    /**
     * 订阅
     *
     * @param characteristic 特征
     * @param enable         是否开启
     */
    fun subscribe(characteristic: BluetoothGattCharacteristic, enable: Boolean) {
        gatt?.setCharacteristicNotification(characteristic, enable)
        // 获取CCCD
        val cccd = characteristic.getDescriptor(ID_CCCD)
        cccd.value = BluetoothGattDescriptor.ENABLE_INDICATION_VALUE
        gatt?.writeDescriptor(cccd)
    }
}


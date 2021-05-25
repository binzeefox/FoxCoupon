package com.binzeefox.foxdevframe_kotlin.utils.device.bluetooth

import android.bluetooth.*

/**
 * 低功耗蓝牙设备GATT回调
 *
 * @author tong.xw
 * 2021/01/28 14:39
 */
internal class BleGattCallbackImpl(private val callback: BleConnectCallback?): BluetoothGattCallback() {
    // Gatt 实例
    var gatt: BluetoothGatt? = null
    // s是否是连接状态
    var isConnected: Boolean = false

    ///////////////////////////////////////////////////////////////////////////
    // 回调
    ///////////////////////////////////////////////////////////////////////////

    override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
        gatt?.also { this.gatt = it }
        when {
            status == BluetoothGatt.GATT_SUCCESS && newState == BluetoothGatt.STATE_DISCONNECTED -> {
                isConnected = false
                callback?.onDisconnected()
            }
            status == BluetoothGatt.GATT_SUCCESS && newState == BluetoothGatt.STATE_CONNECTED -> {
                isConnected = true
                callback?.onConnected()
                gatt?.discoverServices()
            }
            status == 133 -> callback?.onError("连接设备数量超出限制，错误133", null)
        }
    }

    override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
        if (status != BluetoothGatt.GATT_SUCCESS) return
        callback?.onServicesDiscovered(gatt, gatt?.services)
    }

    override fun onCharacteristicRead(gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?, status: Int) {
        callback?.onCharacteristicRead(gatt, characteristic, status)
    }

    override fun onCharacteristicWrite(gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?, status: Int) {
        callback?.onCharacteristicWrite(gatt, characteristic, status)
    }

    override fun onDescriptorRead(gatt: BluetoothGatt?, descriptor: BluetoothGattDescriptor?, status: Int) {
        callback?.onDescriptorRead(gatt, descriptor, status)
    }

    override fun onDescriptorWrite(gatt: BluetoothGatt?, descriptor: BluetoothGattDescriptor?, status: Int) {
        callback?.onDescriptorWrite(gatt, descriptor, status)
    }

    override fun onCharacteristicChanged(gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?) {
        callback?.onCharacteristicChanged(gatt, characteristic)
    }
}
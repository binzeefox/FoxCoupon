package com.binzeefox.foxdevframe_kotlin.utils.device.bluetooth

import android.bluetooth.*

/**
 * 低功耗蓝牙读取回调
 *
 * @author tong.xw
 * 2021/01/28 14:29
 */
interface BleConnectCallback {

    /**
     * 连接成功
     */
    fun onConnected()

    /**
     * 断连成功
     */
    fun onDisconnected()

    /**
     * 错误回调
     */
    fun onError(message: String, throwable: Throwable?)

    /**
     * 发现服务
     *
     * @param gatt        客户端
     * @param serviceList 服务列表
     */
    fun onServicesDiscovered(gatt: BluetoothGatt?, serviceList: List<BluetoothGattService>?)

    /**
     * characteristic读取回调
     *
     * @param gatt           客户端
     * @param characteristic 特征
     * @param status         结果状态
     */
    fun onCharacteristicRead(gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?, status: Int) {

    }

    /**
     * characteristic写入回调
     *
     * @param gatt           客户端
     * @param characteristic 特征，这里为当前特征。
     *                       应在回调总对比该特征内容是否符合期望值。
     *                       若不同，应重发或终止写入
     * @param status         结果状态
     */
    fun onCharacteristicWrite(gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?, status: Int) {

    }

    /**
     * descriptor读取回调
     *
     * @param gatt       客户端
     * @param descriptor 描述
     * @param status     结果状态
     */
    fun onDescriptorRead(gatt: BluetoothGatt?, descriptor: BluetoothGattDescriptor?, status: Int) {

    }

    /**
     * descriptor读取回调
     *
     * @param gatt       客户端
     * @param descriptor 描述
     * @param status     结果状态
     */
    fun onDescriptorWrite(gatt: BluetoothGatt?, descriptor: BluetoothGattDescriptor?, status: Int) {

    }

    /**
     * 接受通知
     */
    fun onCharacteristicChanged(gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?) {

    }
}
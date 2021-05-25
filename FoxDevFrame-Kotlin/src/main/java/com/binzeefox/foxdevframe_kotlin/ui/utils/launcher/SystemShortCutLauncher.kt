package com.binzeefox.foxdevframe_kotlin.ui.utils.launcher

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import com.binzeefox.foxdevframe_kotlin.FoxCore

/**
 * 系统设置页快捷入口
 *
 * @author tong.xw
 * 2021/01/27 15:53
 */
class SystemShortCutLauncher(private val launcher: Launcher) {

    ///////////////////////////////////////////////////////////////////////////
    // 业务方法
    ///////////////////////////////////////////////////////////////////////////
    /**
     * 跳转应用详情信息页
     *
     * @author 狐彻 2020/10/27 8:26
     */
    fun launchApplicationDetails() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", FoxCore.packageName, null)
        intent.data = uri
        launcher.getActivityTarget(intent).commit()
    }

    /**
     * 跳转飞行模式设置界面
     *
     * @author 狐彻 2020/10/27 8:47
     */
    fun launchConnectionSetting() {
        val intent = Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS)
        launcher.getActivityTarget(intent).commit()
    }

    /**
     * 跳转连接与共享设置界面
     *
     * @author 狐彻 2020/10/27 8:47
     */
    fun launchWirelessSettings() {
        val intent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
        launcher.getActivityTarget(intent).commit()
    }

    /**
     * 跳转蓝牙设置
     *
     * @author 狐彻 2020/10/27 8:53
     */
    fun launchBluetoothSettings() {
        val intent = Intent(Settings.ACTION_BLUETOOTH_SETTINGS)
        launcher.getActivityTarget(intent).commit()
    }

    /**
     * 跳转网络设置
     *
     * @author 狐彻 2020/10/27 8:55
     */
    fun launchNetworkSettings() {
        val intent = Intent(Settings.ACTION_NETWORK_OPERATOR_SETTINGS)
        launcher.getActivityTarget(intent).commit()
    }

    /**
     * 跳转定位服务
     *
     * @author 狐彻 2020/10/27 8:56
     */
    fun launchLocationSetting() {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        launcher.getActivityTarget(intent).commit()
    }

    /**
     * 跳转声音设置
     *
     * @author 狐彻 2020/10/27 8:57
     */
    fun launchSoundSetting() {
        val intent = Intent(Settings.ACTION_SOUND_SETTINGS)
        launcher.getActivityTarget(intent).commit()
    }

    /**
     * 跳转wifi设置
     *
     * @author 狐彻 2020/10/27 8:58
     */
    fun launchWifiSetting() {
        val intent = Intent(Settings.ACTION_WIFI_SETTINGS)
        launcher.getActivityTarget(intent).commit()
    }
}
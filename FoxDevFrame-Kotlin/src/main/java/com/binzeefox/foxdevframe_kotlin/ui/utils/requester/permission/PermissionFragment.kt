package com.binzeefox.foxdevframe_kotlin.ui.utils.requester.permission

import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.binzeefox.foxdevframe_kotlin.FoxCore
import com.binzeefox.foxdevframe_kotlin.utils.LogUtil

/**
 * 请求权限专业碎片
 *
 * @author tong.xw
 * 2021/01/26 16:15
 */
class PermissionFragment: Fragment() {
    private val logger: LogUtil = LogUtil("PermissionFragment")
    @Volatile internal var listener: PermissionInterface.OnPermissionResultListener? = null

    /**
     * 检查权限
     */
    internal fun checkPermission(permissionList: Set<String>): List<String> {
        logger.setMessage("checkPermission: 检查权限 => $permissionList")
        val failedList = ArrayList<String>()
        for (permission in permissionList) {
            context.also {
                val result = ActivityCompat.checkSelfPermission(it, permission)
                if (result != PERMISSION_GRANTED) failedList.add(permission)
            }
        }

        return failedList
    }

    /**
     * 请求权限
     */
    internal fun request(requestCode: Int, permissionList: List<String>, listener: PermissionInterface.OnPermissionResultListener) {
        // 若当前监听不为空，则上一次请求未完成，忽略此次请求
        this.listener?.run { return }
        this.listener = listener
        requestPermissions(permissionList.toTypedArray(), requestCode)
    }

    /**
     * 检查是否不再询问
     */
    internal fun checkNoAsk(permission: String): Boolean =
            shouldShowRequestPermissionRationale(permission)

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val failedList = ArrayList<String>()
        val noAskList = ArrayList<String>()
        for (i in grantResults.indices) {
            if (grantResults[i] != PERMISSION_GRANTED) {
                failedList.add(permissions[i])
                if (checkNoAsk(permissions[i]))
                    noAskList.add(permissions[i])
            }
        }

        if (failedList.isNotEmpty())
            logger.setMessage("startCheckAndRequest: 权限未通过 => $failedList").v()
        if (noAskList.isNotEmpty())
            logger.setMessage("startCheckAndRequest: 权限不再询问 => $noAskList").v()
        listener?.onResult(requestCode, failedList, noAskList)
        listener = null
    }

    override fun getContext(): Context {
        var context = super.getContext()
        if (context == null) context = FoxCore.appContext
        return context
    }
}
package com.binzeefox.foxdevframe_kotlin.ui.utils.requester.permission

/**
 * 权限请求相关接口
 *
 * @author tong.xw
 * 2021/01/26 16:13
 */
interface PermissionInterface {

    /**
     * 权限请求回调
     */
    fun interface OnPermissionResultListener {

        /**
         * 权限获取回调
         *
         * @param requestCode 请求码
         * @param failedList  失败权限，包含不再询问的权限
         * @param noAskList   不在询问的权限
         */
        fun onResult(requestCode: Int, failedList: List<String>, noAskList: List<String>)
    }

    /**
     * 添加权限
     */
    fun addPermission(permission: String): PermissionInterface

    /**
     * 批量添加权限
     */
    fun addPermissions(permissionCollection: Collection<String>): PermissionInterface

    /**
     * 仅检查权限
     */
    fun check(requestCode: Int, listener: OnPermissionResultListener)

    /**
     * 检查并请求权限
     */
    fun checkAndRequest(requestCode: Int, listener: OnPermissionResultListener)
}
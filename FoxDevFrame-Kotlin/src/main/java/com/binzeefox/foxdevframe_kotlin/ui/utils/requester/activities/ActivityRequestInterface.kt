package com.binzeefox.foxdevframe_kotlin.ui.utils.requester.activities

import android.content.Intent
import android.os.Bundle

/**
 * 活动请求接口
 *
 * @author tong.xw
 * 2021/01/27 15:19
 */
interface ActivityRequestInterface {

    fun interface OnActivityResultCallback {

        /**
         * 回调
         *
         * @param requestCode   请求码
         * @param resultCode    结果码
         * @param resultData    返回结果
         */
        fun onResult(requestCode: Int, resultCode: Int, resultData: Intent?)
    }

    /**
     * 请求
     *
     * @author 狐彻 2020/10/30 11:39
     */
    fun request(intent: Intent, requestCode: Int, callback: OnActivityResultCallback)
    fun request(intent: Intent, requestCode: Int, callback: OnActivityResultCallback, options: Bundle? = null)
}
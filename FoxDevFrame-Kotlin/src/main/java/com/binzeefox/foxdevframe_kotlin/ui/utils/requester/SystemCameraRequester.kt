package com.binzeefox.foxdevframe_kotlin.ui.utils.requester

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import com.binzeefox.foxdevframe_kotlin.FoxCore
import com.binzeefox.foxdevframe_kotlin.ui.utils.requester.activities.ActivityRequestInterface
import com.binzeefox.foxdevframe_kotlin.ui.utils.requester.activities.ActivityRequester

/**
 * 调用系统相机功能请求器
 *
 * @author tong.xw
 * 2021/01/27 15:35
 */
class SystemCameraRequester(private val requester: ActivityRequester) {

    /**
     * 开启相册
     */
    fun openGallery(requestCode: Int, callback: OnSystemCameraResultCallback) {
        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).let {
            if (it.resolveActivity(FoxCore.packageManager) == null) return
            try {
                requester.request(it, requestCode, createCallback(requestCode, callback), null)
            } catch (e: Exception) {
                callback.onError(e)
            } finally {
                callback.onComplete()
            }
        }
    }

    /**
     * 开启相机
     *
     * @param requestCode 请求码
     * @param cacheUri    缓存路径  若为空则将bitmap返回至data里
     * @param callback    获取回调
     */
    fun openCamera(requestCode: Int, cacheUri: Uri?, callback: OnSystemCameraResultCallback) {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (cacheUri != null) intent.putExtra(MediaStore.EXTRA_OUTPUT, cacheUri)
        try {
            requester.request(intent, requestCode, createCallback(requestCode, callback))
        } catch (e: java.lang.Exception) {
            callback.onError(e)
        } finally {
            callback.onComplete()
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // 内部方法
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 创建监听器
     *
     * @author 狐彻 2020/10/30 17:24
     */
    private fun createCallback(requestCode: Int, callback: OnSystemCameraResultCallback): ActivityRequestInterface.OnActivityResultCallback {
        return ActivityRequestInterface.OnActivityResultCallback { requestCode1: Int, resultCode: Int, resultData: Intent? ->
            when {
                resultCode == Activity.RESULT_CANCELED -> callback.onError(RequestCancelException())
                resultData == null -> callback.onResult(requestCode1, null, null)
                else -> {
                    val uri = resultData.data
                    callback.onResult(requestCode1, resultData, uri)
                }
            }
        }
    }
}


/**
 * 获取结果回调
 */
interface OnSystemCameraResultCallback {
    fun onResult(requestCode: Int, resultData: Intent?, uri: Uri?)

    /**
     * 错误回调
     *
     * @param e RequestCancelException 请求取消会抛出该异常
     * @author tong.xw 2020/12/4 14:04
     */
    fun onError(e: Throwable?)
    fun onComplete()
}

/**
 * 用户取消请求
 *
 * @author 狐彻 2020/10/30 12:02
 */
class RequestCancelException : RuntimeException("获取媒体失败，用户取消")
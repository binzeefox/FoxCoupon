package com.binzeefox.foxdevframe_kotlin.ui

import android.content.Context
import android.view.View
import androidx.annotation.LayoutRes

/**
 * 页面接口
 *
 * @author tong.xw
 * 2021/01/25 16:28
 */
interface UiInterface {

    /**
     * 获取Context
     */
    fun getContext(): Context?

    /**
     * 默认实现 主线程运行
     */
    fun runOnUiThread(action: Runnable)

    fun createContentView(): View? {
        return null
    }

    @LayoutRes
    fun getContentViewResource(): Int {
        return -1
    }
}
package com.binzeefox.foxdevframe_kotlin.utils.device.resources

import com.binzeefox.foxdevframe_kotlin.FoxCore

/**
 * 数值工具
 *
 * @author tong.xw
 * 2021/01/28 15:46
 */
object DimenUtil {

    /**
     * 乘数
     */
    private val density: Float get() = FoxCore.resources.displayMetrics.density

    /**
     * dp转PX
     */
    val Int.dipToPx: Int get() = (this * density + 0.5f).toInt()

    /**
     * px转dp
     */
    val Int.pxToDip: Int get() = (this / density + 0.5f).toInt()
}
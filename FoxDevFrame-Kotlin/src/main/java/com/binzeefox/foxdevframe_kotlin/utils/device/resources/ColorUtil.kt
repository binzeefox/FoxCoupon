package com.binzeefox.foxdevframe_kotlin.utils.device.resources

import android.graphics.Color

/**
 * 色彩工具
 *
 * @author tong.xw
 * 2021/01/28 15:42
 */
object ColorUtil {

    /**
     * 将rgb值转化为#ffffff格式字符串
     */
    fun toColorHexStr(r: Int, g: Int, b: Int): String {
        val color = Color.rgb(r, g, b)
        val colorStr = "000000${Integer.toHexString(color)}"
        return "#${colorStr.substring(colorStr.length - 6)}"
    }

    /**
     * 通过16进制色值获取rgb值
     */
    fun getColorRgb(color: Int): IntArray {
        val red = color and 0xff0000 shr 16
        val green = color and 0x00ff00 shr 8
        val blue = color and 0x0000ff

        return intArrayOf(red, green, blue)
    }
}
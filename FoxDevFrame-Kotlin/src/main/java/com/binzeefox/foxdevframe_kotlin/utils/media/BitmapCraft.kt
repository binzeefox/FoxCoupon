package com.binzeefox.foxdevframe_kotlin.utils.media

import android.graphics.*

/**
 * 位图工作台
 *
 * @author tong.xw
 * 2021/01/29 10:07
 */
class BitmapCraft(private val srcBitmap: Bitmap) {
    private val mMatrix = ColorMatrix()

    /**
     * 创建成品
     */
    fun create(): Bitmap {
        val temp = Bitmap.createBitmap(srcBitmap.width, srcBitmap.height, srcBitmap.config)
        val canvas = Canvas(temp)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            colorFilter = ColorMatrixColorFilter(mMatrix)
        }
        canvas.drawBitmap(srcBitmap, 0F, 0F, paint)
        return temp
    }

    ///////////////////////////////////////////////////////////////////////////
    // 参数方法
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 色调
     */
    fun hue(hue: Float): BitmapCraft {
        val matrix = ColorMatrix()
        matrix.setRotate(0, hue)
        matrix.setRotate(1, hue)
        matrix.setRotate(2, hue)

        mMatrix.postConcat(matrix)
        return this
    }

    /**
     * 饱和度
     */
    fun saturation(saturation: Float): BitmapCraft {
        val matrix = ColorMatrix()
        matrix.setSaturation(saturation)
        mMatrix.postConcat(matrix)
        return this
    }

    /**
     * 亮度
     */
    fun lum(lum: Float): BitmapCraft {
        val matrix = ColorMatrix()
        matrix.setScale(lum, lum, lum, 1f)
        mMatrix.postConcat(matrix)
        return this
    }
}
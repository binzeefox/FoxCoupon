package com.binzeefox.foxdevframe_kotlin.utils.media

import android.graphics.Bitmap
import android.renderscript.*
import com.binzeefox.foxdevframe_kotlin.FoxCore
import kotlin.math.cos
import kotlin.math.sin

/**
 * RenderScriptTools工具方法
 *
 * @author tong.xw
 * 2021/01/29 10:15
 */
object RenderScriptTools {

    /**
     * 高斯模糊
     */
    fun Bitmap.blurBitmap(radius: Float): Bitmap {
        val rs = RenderScript.create(FoxCore.appContext)
        val temp = copy(config, true)
        val input = Allocation.createFromBitmap(rs, this)
        val output = Allocation.createTyped(rs, input.type)
        val script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))

        script.setRadius(radius)
        script.setInput(input)
        script.forEach(output)
        output.copyTo(temp)
        rs.destroy()
        return temp
    }

    /**
     * 改变色调
     */
    fun Bitmap.hueBitmap(hue: Float): Bitmap {
        val rs = RenderScript.create(FoxCore.appContext)
        val temp = copy(config, true)
        val input = Allocation.createFromBitmap(rs, this)
        val output = Allocation.createTyped(rs, input.type)
        val script = ScriptIntrinsicColorMatrix.create(rs)

        val cos = cos(hue)
        val sin = sin(hue)
        val mat = Matrix3f()
        mat[0, 0] = (.299 + .701 * cos + .168 * sin).toFloat()
        mat[1, 0] = (.587 - .587 * cos + .330 * sin).toFloat()
        mat[2, 0] = (.114 - .114 * cos - .497 * sin).toFloat()
        mat[0, 1] = (.299 - .299 * cos - .328 * sin).toFloat()
        mat[1, 1] = (.587 + .413 * cos + .035 * sin).toFloat()
        mat[2, 1] = (.114 - .114 * cos + .292 * sin).toFloat()
        mat[0, 2] = (.299 - .3 * cos + 1.25 * sin).toFloat()
        mat[1, 2] = (.587 - .588 * cos - 1.05 * sin).toFloat()
        mat[2, 2] = (.114 + .886 * cos - .203 * sin).toFloat()
        script.setColorMatrix(mat)
        script.forEach(input, output)
        output.copyTo(temp)
        rs.destroy()
        return temp
    }
}
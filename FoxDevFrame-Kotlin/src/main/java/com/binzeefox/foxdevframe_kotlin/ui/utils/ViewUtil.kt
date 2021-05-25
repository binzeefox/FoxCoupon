package com.binzeefox.foxdevframe_kotlin.ui.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import com.binzeefox.foxdevframe_kotlin.R
import com.binzeefox.foxdevframe_kotlin.utils.LogUtil
import com.binzeefox.foxdevframe_kotlin.utils.ThreadUtils
import java.lang.Exception

/**
 * 控件工具
 *
 * @author tong.xw
 * 2021/01/26 10:05
 */
class ViewUtil(private val target: View) {

    /**
     * 防抖点击事件
     */
    fun setOnDeBounceClickListener(skip: Long, listener: View.OnClickListener) {
        target.setOnClickListener(object : View.OnClickListener {
            val _skip = skip

            override fun onClick(v: View?) {
                v ?: return
                val curTimeStamp = System.currentTimeMillis()
                var tagTimeStamp = -1L
                v.getTag(R.id.fox_frame_kotlin_view_util_debounce_id)?.apply {
                    tagTimeStamp = this as Long
                }
                if (tagTimeStamp != -1L || tagTimeStamp + _skip < curTimeStamp) {
                    v.setTag(R.id.fox_frame_kotlin_view_util_debounce_id, curTimeStamp)
                    listener.onClick(v)
                }
            }
        })
    }

    /**
     * 获取View截图
     */
    fun captureViewShot(): Bitmap {
        val bitmap = Bitmap.createBitmap(target.width, target.height, Bitmap.Config.RGB_565)
        val canvas = Canvas(bitmap)
        target.layout(target.left, target.top, target.right, target.bottom)
        val backDrawable = target.background
        if (backDrawable == null)
            canvas.drawColor(Color.TRANSPARENT)
        else backDrawable.draw(canvas)

        target.draw(canvas)
        return bitmap
    }

    /**
     * 开启迪斯科模式
     */
    fun discoMode() {
        val thread = HandlerThread("view_util_disco")
        thread.start()

        fun discoModeValueFnc(values: IntArray) {
            values[0] += values[1]
            if (values[0] > 255) {
                values[0] = 255
                values[1] *= -1
            }
            if (values[0] < 0) {
                values[0] = 0
                values[1] *= -1
            }
        }

        Handler(thread.looper).post {
            val r = intArrayOf(0, 1)
            val g = intArrayOf(0, 10)
            val b = intArrayOf(0, 20)

            try {
                while (true) {
                    Thread.sleep(5)
                    discoModeValueFnc(r)
                    discoModeValueFnc(g)
                    discoModeValueFnc(b)
                    ThreadUtils.runOnUiThread {
                        val colorValue = Color.argb(255, r[0], g[0], b[0])
                        target.setBackgroundColor(colorValue)
                    }
                }
            } catch (e: Exception) {
                LogUtil("DiscoMode").setThrowable(e).e()
            }
        }
    }
}
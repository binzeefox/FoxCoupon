package com.binzeefox.foxdevframe_kotlin

import android.content.Context
import androidx.startup.Initializer

/**
 * 初始化工具
 *
 * @author tong.xw
 * 2021/01/25 14:48
 */
class FoxDevFrameInitializer: Initializer<FoxCore> {

    override fun create(context: Context): FoxCore {
        FoxCore.init(context)
        return FoxCore
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf()
    }
}
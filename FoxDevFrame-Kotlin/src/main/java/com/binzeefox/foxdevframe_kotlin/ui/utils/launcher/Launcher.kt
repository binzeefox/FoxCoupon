package com.binzeefox.foxdevframe_kotlin.ui.utils.launcher

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.binzeefox.foxdevframe_kotlin.FoxCore
import com.binzeefox.foxdevframe_kotlin.ui.utils.launcher.targets.ActivityTarget

/**
 * 页面跳转工具
 *
 * @author tong.xw
 * 2021/01/27 15:46
 */
@Suppress("MemberVisibilityCanBePrivate")
class Launcher(private val ctx: Context) {

    companion object {
        val byTopActivity: Launcher get() = Launcher(FoxCore.simulatedBackStack.peek())
        val byApplication: Launcher get() = Launcher(FoxCore.appContext)
    }

    /**
     * 跳转系统页面工具
     */
    fun systemShortCuts() = SystemShortCutLauncher(this)

    /**
     * 给定Intent跳转至Activity
     *
     * @param intent 意图
     */
    fun getActivityTarget(intent: Intent): ILauncherTarget {
        return getActivityTarget(intent, null)
    }

    /**
     * 给定Intent跳转至Activity
     *
     * @param intent  意图
     * @param options startActivity()的第二个参数
     */
    fun getActivityTarget(intent: Intent, options: Bundle?): ILauncherTarget {
        return ActivityTarget(ctx, intent, options)
    }

    /**
     * 跳转Activity
     *
     * @param cls 目标Activity
     */
    fun getActivityTarget(cls: Class<out Activity?>): ILauncherTarget {
        return getActivityTarget(cls, null)
    }

    /**
     * 跳转Activity
     *
     * @param cls 目标Activity
     * @param options startActivity()的第二个参数
     */
    fun getActivityTarget(cls: Class<out Activity?>, options: Bundle?): ILauncherTarget {
        val intent = Intent(ctx, cls)
        return ActivityTarget(ctx, intent, options)
    }
}

/**
 * 跳转目标接口
 */
interface ILauncherTarget {
    /**
     * intent拦截器，用于在跳转前对intent进行最终操作，比如填入Extra等
     *
     * @param interceptor 拦截器
     */
    fun intentInterceptor(interceptor: IntentInterceptor): ILauncherTarget

    /**
     * 开始跳转
     */
    fun commit()

    /**
     * 拦截器
     */
    fun interface IntentInterceptor {
        /**
         * 拦截回调，返回的Intent将用于跳转
         *
         * @return 最终用于跳转的Intent，若返回null则终止跳转
         */
        fun onIntercept(intent: Intent): Intent
    }
}
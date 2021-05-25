@file:Suppress("MemberVisibilityCanBePrivate")

package com.binzeefox.foxdevframe_kotlin

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import java.lang.RuntimeException
import java.util.*

/**
 * 核心类
 *
 * - 提供静态Activity单例
 * - 提供应用信息
 * - 注册LifeCycle
 *
 * @author tong.xw
 * 2021/01/25 14:49
 */
object FoxCore {

    // 全局ApplicationContext
    lateinit var appContext: Context

    // 资源文件
    val resources: Resources
        get() {
            checkAppContext()
            return appContext.resources
        }

    // 模拟返回栈
    val simulatedBackStack: ActivityStack = ActivityStack()

    // 包名
    val packageName: String
        get() {
            checkAppContext()
            return appContext.packageName
        }

    // 包管理器
    val packageManager: PackageManager
        get() {
            checkAppContext()
            return appContext.packageManager
        }

    // 包信息
    val packageInfo: PackageInfo
        get() = packageManager.getPackageInfo(packageName, 0)

    // 应用版本名
    val versionName: String get() = packageInfo.versionName

    // 应用版本号
    val versionCode: Long
        get() {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
                packageInfo.longVersionCode
            else packageInfo.versionCode.toLong()
        }

    ///////////////////////////////////////////////////////////////////////////
    // 业务方法
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 初始化
     */
    internal fun init(context: Context) {
        appContext = context.applicationContext
        registerActivityCallback()

        // 本地化多语言设置
        val languageTag = FoxConfigs.readLanguageTag()?.apply {
            val locale = Locale.forLanguageTag(this)
            setLocale(locale, false)
        }
    }

    /**
     * 设置语言
     *
     * @param locale 语言
     * @param localize 是否本地化操作
     * FIXME DEPRECATED
     */
    @Deprecated(message = "切换语言的方法已经过时，暂时未适配高版本做法")
    fun setLocale(locale: Locale, localize: Boolean) {
        val displayMetrics = resources.displayMetrics
        val configuration = resources.configuration

        configuration.setLocale(locale)
        resources.updateConfiguration(configuration, displayMetrics)
        if (localize) FoxConfigs.writeLanguageTag(locale.toLanguageTag())
    }

    ///////////////////////////////////////////////////////////////////////////
    // 私有方法
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 检查是否初始化，若未初始化应报错
     */
    private fun checkAppContext() {
        if (!this::appContext.isInitialized)
            throw UnInitializedException()
    }

    /**
     * 注册Activity回调\
     */
    private fun registerActivityCallback() {
        checkAppContext()
        (appContext as Application).registerActivityLifecycleCallbacks(object: FoxActivityLifecycleCallback() {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                super.onActivityCreated(activity, savedInstanceState)
                // 入栈
                simulatedBackStack.push(activity)
            }

            override fun onActivityResumed(activity: Activity) {
                super.onActivityResumed(activity)
                // 状态可见，转入栈顶
                simulatedBackStack.moveToTop(activity)
            }

            override fun onActivityDestroyed(activity: Activity) {
                super.onActivityDestroyed(activity)
                // 回收
                simulatedBackStack.superRemove(activity)
            }
        })
    }

    ///////////////////////////////////////////////////////////////////////////
    // 内部类
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 模拟的返回栈
     */
    class ActivityStack : Stack<Activity>() {

        @Synchronized
        override fun pop(): Activity {
            return super.pop().also { it.finish() }
        }

        @Synchronized
        override fun removeAt(index: Int): Activity {
            return super.removeAt(index).also { it.finish() }
        }

        @Synchronized
        override fun remove(element: Activity?): Boolean {
            element?.finish()
            return super.remove(element)
        }

        ///////////////////////////////////////////////////////////////////////////
        // 私有方法
        ///////////////////////////////////////////////////////////////////////////

        /**
         * 移至栈顶
         */
        internal fun moveToTop(activity: Activity) {
            super.remove(activity)
            push(activity)
        }

        /**
         * 代理弗雷的移除方法
         */
        internal fun superRemove(activity: Activity) {
            super.remove(activity)
        }
    }

    /**
     * 未初始化异常
     */
    private class UnInitializedException : RuntimeException("未初始化，请调用 FoxCore.init(Context) 进行初始化")
}
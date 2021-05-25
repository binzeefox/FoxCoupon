package com.binzeefox.foxdevframe_kotlin

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.binzeefox.foxdevframe_kotlin.utils.LogUtil

/**
 * Activity生命周期监听器
 *
 * @author tong.xw
 * 2021/01/25 16:19
 */
open class FoxActivityLifecycleCallback: Application.ActivityLifecycleCallbacks {

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        log(activity, "onCreate")
    }

    override fun onActivityStarted(activity: Activity) {
        log(activity, "onStarted")
    }

    override fun onActivityResumed(activity: Activity) {
        log(activity, "onResumed")
    }

    override fun onActivityPaused(activity: Activity) {
        log(activity, "onPaused")
    }

    override fun onActivityStopped(activity: Activity) {
        log(activity, "onStopped")
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        log(activity, "onSaveInstanceState")
    }

    override fun onActivityDestroyed(activity: Activity) {
        log(activity, "onDestroyed")
    }

    /**
     * 打印
     */
    protected open fun log(activity: Activity, status: String) {
        LogUtil("FoxLifecycleCallback").setMessage("$status: ${activity::class.qualifiedName}")
    }
}
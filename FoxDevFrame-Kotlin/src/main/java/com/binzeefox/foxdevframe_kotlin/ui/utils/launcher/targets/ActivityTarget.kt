package com.binzeefox.foxdevframe_kotlin.ui.utils.launcher.targets

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.binzeefox.foxdevframe_kotlin.ui.utils.launcher.ILauncherTarget

/**
 * 目标Activity的跳转
 *
 * @author tong.xw
 * 2021/01/27 15:55
 */
class ActivityTarget(
        private val ctx: Context,
        private var intent: Intent,
        private val options: Bundle?
): ILauncherTarget {

    override fun intentInterceptor(interceptor: ILauncherTarget.IntentInterceptor): ILauncherTarget {
        intent = interceptor.onIntercept(intent)
        return this
    }

    override fun commit() {
        if (ctx is Application) intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        ctx.startActivity(intent, options)
    }
}
package com.binzeefox.foxdevframe_kotlin.ui.utils.requester.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.binzeefox.foxdevframe_kotlin.ui.utils.requester.BaseRequester

/**
 * 活动请求工具
 *
 * 专门处理
 * @see androidx.core.app.ActivityCompat.startActivityForResult
 * @author tong.xw
 * 2021/01/27 15:24
 */
class ActivityRequester(fm: FragmentManager): BaseRequester(fm), ActivityRequestInterface {
    // 碎片
    private val mFragment: ActivityRequestFragment
        get() = childFragment as ActivityRequestFragment

    /**
     * 副构造器
     */
    constructor(activity: AppCompatActivity) : this(activity.supportFragmentManager)

    /**
     * 副构造器
     */
    constructor(fragment: Fragment) : this(fragment.childFragmentManager)

    override fun getFragmentTag(): String = "Fox-activity-requester"

    override fun createFragment(): Fragment = ActivityRequestFragment()

    override fun request(intent: Intent, requestCode: Int, callback: ActivityRequestInterface.OnActivityResultCallback) =
            request(intent, requestCode, callback, null)

    override fun request(intent: Intent, requestCode: Int, callback: ActivityRequestInterface.OnActivityResultCallback, options: Bundle?) {
        mFragment.listener = callback
        options?.also {
            mFragment.startActivityForResult(intent, requestCode, options)
        }?: run {
            mFragment.startActivityForResult(intent, requestCode)
        }
    }
}
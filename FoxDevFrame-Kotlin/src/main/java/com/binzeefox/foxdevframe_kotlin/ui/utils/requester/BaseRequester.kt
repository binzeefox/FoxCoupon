package com.binzeefox.foxdevframe_kotlin.ui.utils.requester

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

/**
 * 利用无页面Fragment请求数据的工具基类
 *
 * @author tong.xw
 * 2021/01/26 15:06
 */
abstract class BaseRequester(private val fm: FragmentManager) {

    var childFragment: Fragment private set

    init {
        var fragment = fm.findFragmentByTag(this.getFragmentTag())
        fragment ?: kotlin.run {
            fragment = createFragment()
            fm.beginTransaction()
                    .add(fragment!!, getFragmentTag())
                    .commitNow()
        }
        childFragment = fragment!!
    }

    protected abstract fun getFragmentTag(): String

    protected abstract fun createFragment(): Fragment
}
package com.binzeefox.foxdevframe_kotlin.ui.utils.requester.permission

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.binzeefox.foxdevframe_kotlin.ui.utils.requester.BaseRequester
import java.security.Permission

/**
 * 权限工具
 *
 * @author tong.xw
 * 2021/01/26 16:30
 */
class PermissionUtil(fragmentManager: FragmentManager) : BaseRequester(fragmentManager), PermissionInterface {

    // 待处理权限容器
    private val permissionSet = HashSet<String>()
    // 碎片
    private val mFragment: PermissionFragment
        get() = childFragment as PermissionFragment


    /**
     * 副构造器
     */
    constructor(activity: AppCompatActivity) : this(activity.supportFragmentManager)

    /**
     * 副构造器
     */
    constructor(fragment: Fragment) : this(fragment.childFragmentManager)

    override fun getFragmentTag(): String = "Fox-Permission-util"

    override fun createFragment(): Fragment = PermissionFragment()

    override fun addPermission(permission: String): PermissionInterface {
        permissionSet.add(permission)
        return this
    }

    override fun addPermissions(permissionCollection: Collection<String>): PermissionInterface {
        permissionSet.addAll(permissionCollection)
        return this
    }

    override fun check(requestCode: Int, listener: PermissionInterface.OnPermissionResultListener) {
        val failedList = mFragment.checkPermission(permissionSet)
        val noAskList = ArrayList<String>()
        for (permission in failedList) {
            if (mFragment.checkNoAsk(permission))
                noAskList.add(permission)
        }
        listener.onResult(requestCode, failedList, noAskList)
    }

    override fun checkAndRequest(requestCode: Int, listener: PermissionInterface.OnPermissionResultListener) {
        val failedList = mFragment.checkPermission(permissionSet)
        if (failedList.isEmpty()) listener.onResult(requestCode, ArrayList(), ArrayList())
        else mFragment.request(requestCode, failedList, listener)
    }
}
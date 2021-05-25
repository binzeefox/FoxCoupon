package com.binzeefox.foxdevframe_kotlin.ui.utils.requester.activities

import android.content.Intent
import androidx.fragment.app.Fragment

/**
 * 活动请求业务碎片
 *
 * @author tong.xw
 * 2021/01/27 15:21
 */
class ActivityRequestFragment: Fragment() {
    @Volatile internal var listener: ActivityRequestInterface.OnActivityResultCallback? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        listener?.onResult(requestCode, resultCode, data)
        listener = null
    }
}
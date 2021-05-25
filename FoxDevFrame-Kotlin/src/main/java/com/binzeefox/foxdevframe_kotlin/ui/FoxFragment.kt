package com.binzeefox.foxdevframe_kotlin.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

/**
 * 碎片基类
 *
 * @author tong.xw
 * 2021/01/25 16:43
 */
abstract class FoxFragment: Fragment(), UiInterface {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = createContentView()
        return view ?: if (getContentViewResource() != -1) {
                    inflater.inflate(getContentViewResource(), container, false)
                } else super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun runOnUiThread(action: Runnable) {
        val handler = Handler(Looper.getMainLooper())
        if (Looper.getMainLooper().isCurrentThread) action.run()
        else handler.post(action)
    }
}
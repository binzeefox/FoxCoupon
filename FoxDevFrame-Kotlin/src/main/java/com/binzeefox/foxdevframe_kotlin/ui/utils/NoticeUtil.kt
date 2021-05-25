package com.binzeefox.foxdevframe_kotlin.ui.utils

import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.StringRes
import com.binzeefox.foxdevframe_kotlin.FoxCore
import com.google.android.material.snackbar.BaseTransientBottomBar.BaseCallback
import com.google.android.material.snackbar.Snackbar

/**
 * 提醒工具
 *
 * @author tong.xw
 * 2021/01/26 14:34
 */
object NoticeUtil {
    // 上下文实例
    private val context: Context
        get() {
            return if (FoxCore.simulatedBackStack.peek() != null)
                FoxCore.simulatedBackStack.peek()
            else FoxCore.appContext
        }


    // Toast实例
    private var mToast: Toast? = null

    // Snackbar实例
    private var mSnackbar: Snackbar? = null

    ///////////////////////////////////////////////////////////////////////////
    // Toast
    ///////////////////////////////////////////////////////////////////////////

    fun toast(toast: Toast): Operation = createOperation(toast)

    fun toast(msg: CharSequence): Operation = createOperation(Toast.makeText(context, msg, Toast.LENGTH_SHORT))

    fun toast(@StringRes resourceId: Int): Operation = createOperation(Toast.makeText(context, resourceId, Toast.LENGTH_SHORT))

    fun toast(): Operation = toast("")

    ///////////////////////////////////////////////////////////////////////////
    // TODO Snackbar
    ///////////////////////////////////////////////////////////////////////////


    ///////////////////////////////////////////////////////////////////////////
    // 内部方法
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 创建Operation
     */
    private fun createOperation(toast: Toast) = ToastOperation(toast)

    /**
     * 创建Operation
     */
    private fun createOperation(snackbar: Snackbar) = SnackbarOperation(snackbar)

    ///////////////////////////////////////////////////////////////////////////
    // 内部类
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 提醒操作
     */
    interface Operation {
        fun setDuration(duration: Int): Operation
        fun setMessage(msg: CharSequence): Operation
        fun setMessage(@StringRes resourceId: Int): Operation
        fun showNow()
        fun showOnLastHide()
    }

    /**
     * Toast操作
     */
    class ToastOperation internal constructor(private val toast: Toast) : NoticeUtil.Operation {

        override fun setDuration(duration: Int): NoticeUtil.Operation = apply {
            toast.duration = duration
        }

        override fun setMessage(msg: CharSequence): NoticeUtil.Operation = apply {
            toast.setText(msg)
        }

        override fun setMessage(resourceId: Int): NoticeUtil.Operation = apply {
            toast.setText(resourceId)
        }

        override fun showNow() {
            when {
                mToast == null -> {
                    mToast = toast
                    mToast?.show()
                }
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> {
                    mToast?.addCallback(object : Toast.Callback() {
                        override fun onToastHidden() {
                            mToast = toast
                            mToast?.show()
                        }
                    })
                }
                else -> {
                    mToast = toast
                    mToast?.show()
                }
            }
        }

        override fun showOnLastHide() {
            when {
                mToast == null -> {
                    mToast = toast
                    mToast!!.show()
                }
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> {
                    mToast!!.addCallback(object : Toast.Callback() {
                        override fun onToastHidden() {
                            mToast = toast
                            mToast!!.show()
                        }
                    })
                    mToast!!.cancel()
                }
                else -> {
                    mToast!!.cancel()
                    mToast = toast
                    mToast!!.show()
                }
            }
        }

    }

    /**
     * Snackbar操作
     */
    class SnackbarOperation internal constructor(private val snackbar: Snackbar) : Operation {
        override fun setDuration(duration: Int): Operation {
            snackbar.duration = duration
            return this
        }

        override fun setMessage(msg: CharSequence): Operation {
            snackbar.setText(msg)
            return this
        }

        override fun setMessage(resourceId: Int): Operation {
            snackbar.setText(resourceId)
            return this
        }

        /**
         * 当上一个Toast结束时显示
         *
         * @author tong.xw 2020/12/25 11:26
         */
        override fun showOnLastHide() {
            when {
                mSnackbar == null -> {
                    mSnackbar = snackbar
                    mSnackbar!!.show()
                }
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> {
                    mSnackbar!!.addCallback(object : BaseCallback<Snackbar?>() {
                        override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                            mSnackbar = snackbar
                            snackbar.show()
                        }
                    })
                }
                else -> {
                    mSnackbar = snackbar
                    snackbar.show()
                }
            }
        }

        /**
         * 取消上一个toast立即显示
         *
         * @author tong.xw 2020/12/25 11:26
         */
        override fun showNow() {
            when {
                mSnackbar == null -> {
                    mSnackbar = snackbar
                    snackbar.show()
                }
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> {
                    mSnackbar!!.addCallback(object : BaseCallback<Snackbar?>() {
                        override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                            mSnackbar = snackbar
                            snackbar.show()
                        }
                    })
                    mSnackbar!!.dismiss()
                }
                else -> {
                    mSnackbar!!.dismiss()
                    mSnackbar = snackbar
                    snackbar.show()
                }
            }
        }
    }
}


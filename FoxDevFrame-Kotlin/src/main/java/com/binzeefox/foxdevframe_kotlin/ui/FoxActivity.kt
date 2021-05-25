package com.binzeefox.foxdevframe_kotlin.ui

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity

/**
 * Activity基类
 *
 * @author tong.xw
 * 2021/01/25 16:31
 */
abstract class FoxActivity: AppCompatActivity(), UiInterface {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initContentView()
        onCreate()
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        initContentView()
        onCreate()
    }

    override fun getContext(): Context = this

    ///////////////////////////////////////////////////////////////////////////
    // 保护方法
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 代理onCreate
     *
     * {@link FoxActivity#onCreate(Bundle)}
     * 和
     * {@link FoxActivity#onCreate(Bundle, PersistableBundle)}
     * 方法在调用 {@link FoxActivity#initContentView()} 后都会调用这个方法
     */
    protected open fun onCreate() {

    }

    /**
     * 设置全屏
     */
    protected fun setFullScreen() {
        val window = window
        val decorView = window.decorView
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val controller = decorView.windowInsetsController
            controller!!.hide(WindowInsets.Type.navigationBars())
        } else {
            val option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            decorView.systemUiVisibility = option
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // 私有方法
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 初始化ContentView
     *
     * View实例优先级高于资源文件
     */
    private fun initContentView() {
        val layout = createContentView()
        layout?.also {
            setContentView(it)
        }
        layout?:run {
            if (getContentViewResource() != -1)
                setContentView(getContentViewResource())
        }
    }
}
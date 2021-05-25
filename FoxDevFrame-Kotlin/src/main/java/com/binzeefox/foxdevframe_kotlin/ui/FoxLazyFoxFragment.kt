package com.binzeefox.foxdevframe_kotlin.ui

/**
 * 懒加载碎片
 *
 * @author tong.xw
 * 2021/01/25 16:57
 */
@Suppress("MemberVisibilityCanBePrivate")
abstract class FoxLazyFoxFragment: FoxFragment() {
    private var isLoaded = false    // 是否已加载

    override fun onResume() {
        super.onResume()
        checkLoad()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        checkLoad()
    }

    override fun onDestroy() {
        super.onDestroy()
        isLoaded = false
    }

    /**
     * 设置加载状态
     */
    fun setLoaded(isLoaded: Boolean) {
        this.isLoaded = isLoaded
    }

    ///////////////////////////////////////////////////////////////////////////
    // 内部方法
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 懒加载
     */
    protected abstract fun onLoad()

    /**
     * 检查加载状态
     */
    protected fun checkLoad() {
        if (isVisible && isLoaded) {
            onLoad()
            isLoaded = true
        }
    }
}
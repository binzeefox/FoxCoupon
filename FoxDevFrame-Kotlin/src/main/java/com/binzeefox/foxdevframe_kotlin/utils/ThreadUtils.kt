package com.binzeefox.foxdevframe_kotlin.utils

import android.os.Handler
import android.os.Looper
import java.util.concurrent.*

/**
 * 线程工具
 *
 * @author tong.xw
 * 2021/01/26 10:28
 */
object ThreadUtils {
    private lateinit var mIOUtil: ConcurrentUtil    //IO
    private lateinit var mComputationUtil: ConcurrentUtil    //计算
    private val mOtherUtilMap: ConcurrentHashMap<String, ConcurrentUtil> =
            ConcurrentHashMap<String, ConcurrentUtil>() //其它线程池工具

    /**
     * UI运行
     */
    fun runOnUiThread(action: Runnable) {
        if (Looper.getMainLooper().isCurrentThread)
            action.run()
        else Handler(Looper.getMainLooper()).post(action)
    }

    /**
     * IO线程运行
     */
    fun executeIO(work: Runnable) {
        initIOExecutor()
        mIOUtil.execute(work)
    }

    /**
     * IO线程获取Future
     */
    fun <T> callIO(callable: Callable<T>): Future<T> {
        initIOExecutor()
        return mIOUtil.call(callable)
    }

    /**
     * 计算线程运行
     */
    fun executeComputation(work: Runnable) {
        initIOExecutor()
        mIOUtil.execute(work)
    }

    /**
     * 计算线程获取Future
     */
    fun <T> callComputation(callable: Callable<T>): Future<T> {
        initIOExecutor()
        return mIOUtil.call(callable)
    }

    /**
     * 自定义线程运行
     *
     * @param defaultExecutor 默认线程池，若tag下无线程池则使用该线程池。若为空则默认为CachedThreadPool
     */
    fun executeOther(tag: String, work: Runnable, defaultExecutor: ExecutorService?) {
        val util = getOtherExecutor(tag, defaultExecutor)
        util.execute(work)
    }

    /**
     * 自定义线程
     */
    fun executeOther(tag: String, work: Runnable) = ThreadUtils.executeOther(tag, work, null)

    /**
     * 获取自定义线程Future
     *
     * @param defaultExecutor 默认线程池，若tag下无线程池则使用该线程池。若为空则默认为CachedThreadPool
     */
    fun <T> callOther(tag: String, callable: Callable<T>, defaultExecutor: ExecutorService?): Future<T> {
        val util = getOtherExecutor(tag, defaultExecutor)
        return util.call(callable)
    }

    /**
     * 获取自定义线程Future
     */
    fun <T> callOther(tag: String, callable: Callable<T>): Future<T> = callOther(tag, callable, null)

    /**
     * 关闭自定义线程池
     */
    fun shutdownExecutor(tag: String) {
        val util = mOtherUtilMap[tag]
        util?.also {
            it.shutdown()
            mOtherUtilMap.remove(tag)
        }
    }

    /**
     * 立即自定义关闭线程池
     */
    fun shutdownExecutorNow(tag: String) {
        val util = mOtherUtilMap[tag] ?: return
        util.shutdownNow()
        mOtherUtilMap.remove(tag)
    }

    /**
     * 立即回收所有任务
     *
     * @author tong.xw 2020/12/24 16:18
     */
    fun shutdownAllNow() {
        for (util in mOtherUtilMap.values) {
            util.shutdownNow()
        }
        mOtherUtilMap.clear()
        mIOUtil.shutdownNow()
        mComputationUtil.shutdownNow()
    }

    ///////////////////////////////////////////////////////////////////////////
    // 私有方法
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 初始化IO线程
     */
    private fun initIOExecutor() {
        if (!this::mIOUtil.isInitialized || mIOUtil.isRecycled) {
            ThreadPoolExecutor(1, Int.MAX_VALUE, 1, TimeUnit.SECONDS, SynchronousQueue()).also {
                // 一个常驻线程，无限个缓存线程
                mIOUtil = ConcurrentUtil(it)
            }
        }
    }

    /**
     * 初始化计算线程
     */
    private fun initComputationExecutor() {
        if (!this::mComputationUtil.isInitialized || mComputationUtil.isRecycled) {
            Executors.newCachedThreadPool().also {
                // 一个常驻线程，无限个缓存线程
                mComputationUtil = ConcurrentUtil(it)
            }
        }
    }

    /**
     * 获取自定义线程
     */
    private fun getOtherExecutor(tag: String, defaultExecutor: ExecutorService?): ConcurrentUtil {
        var util: ConcurrentUtil? = mOtherUtilMap[tag]
        util?.run { return util as ConcurrentUtil }
        defaultExecutor?.run {
            util = ConcurrentUtil(defaultExecutor)
            mOtherUtilMap[tag] = util!!
            return util!!
        }
        util = ConcurrentUtil(Executors.newCachedThreadPool())
        mOtherUtilMap[tag] = util!!
        return util!!
    }
}
package com.binzeefox.foxdevframe_kotlin.utils

import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Future

/**
 * 并发工具
 *
 * @author tong.xw
 * 2021/01/26 10:31
 */
class ConcurrentUtil(private val executor: ExecutorService) {

    /**
     * 是否已经 回收
     */
    val isRecycled: Boolean get() = executor.isShutdown || executor.isTerminated

    /**
     * 线程运行
     */
    fun execute(runnable: Runnable) = executor.execute(runnable)

    /**
     * 获取请求Future
     */
    fun <T> call(callable: Callable<T>): Future<T> = executor.submit(callable)

    fun shutdown() = executor.shutdown()

    fun shutdownNow() = executor.shutdownNow()
}
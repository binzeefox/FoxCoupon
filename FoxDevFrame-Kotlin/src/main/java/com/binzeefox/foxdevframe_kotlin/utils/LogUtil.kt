package com.binzeefox.foxdevframe_kotlin.utils

import android.os.Looper
import android.util.Log
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors

/**
 * 日志工具
 *
 * @author tong.xw
 * 2021/01/25 15:44
 */
const val CLASS_NONE = -1 //curClass为此时，不打印Log
const val CLASS_E = 0 //curClass为此时，只打印E
const val CLASS_W = 1 //curClass为此时，只打印E和W
const val CLASS_D = 2 //curClass为此时，只打印E,W和D
const val CLASS_I = 3 //curClass为此时，只打印E,W,D和I
const val CLASS_V = 4 //curClass为此时，全部打印

class LogUtil(private val tag: String) {
    
    // 日志线程
    private val logExecutor = Executors.newSingleThreadExecutor()
    
    // 信息
    private var msg: String? = null
    
    // 异常
    private var throwable: Throwable? = null

    ///////////////////////////////////////////////////////////////////////////
    // 工具方法
    ///////////////////////////////////////////////////////////////////////////
    
    /**
     * 添加信息
     */
    fun setMessage(msg: String?): LogUtil {
        this.msg = msg
        return this
    }
    
    /**
     * 添加异常信息
     */
    fun setThrowable(throwable: Throwable): LogUtil {
        this.throwable = throwable
        return this
    }

    ///////////////////////////////////////////////////////////////////////////
    // 打印方法
    ///////////////////////////////////////////////////////////////////////////

    // v
    fun v() {
        if (curClass >= CLASS_V) {
            Log.v(tag, msg, throwable)
            recordLog("V", tag, msg, throwable)
        }
    }

    // d
    fun d() {
        if (curClass >= CLASS_D) {
            Log.d(tag, msg, throwable)
            recordLog("D", tag, msg, throwable)
        }
    }

    // i
    fun i() {
        if (curClass >= CLASS_I) {
            Log.i(tag, msg, throwable)
            recordLog("I", tag, msg, throwable)
        }
    }

    // w
    fun w() {
        if (curClass >= CLASS_W) {
            Log.w(tag, msg, throwable)
            recordLog("W", tag, msg, throwable)
        }
    }

    // e
    fun e() {
        if (curClass >= CLASS_E) {
            Log.e(tag, msg, throwable)
            recordLog("E", tag, msg, throwable)
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // 私有方法
    ///////////////////////////////////////////////////////////////////////////
    /**
     * 记录本地化
     */
    private fun recordLog(level: String, tag: String, message: String?, t: Throwable?) {
        // 读写锁在该方法的调用方法中实现
        logExecutor.execute {
            synchronized(LogUtil::class.java) {
                val log = FoxLog(level, tag, message, t)
                if (lastLog != null) {
                    log.preview = lastLog
                }
                lastLog = log
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // 静态
    ///////////////////////////////////////////////////////////////////////////
    
    companion object {

        // 打印指示器
        val curClass = CLASS_V

        // 单链表倒叙
        private var lastLog: FoxLog? = null

        ///////////////////////////////////////////////////////////////////////////
        // 工具方法
        ///////////////////////////////////////////////////////////////////////////

        /**
         * 同步方法 ！！主线程警告！！ <p>
         * 获取利用该类打印的所有可见日志的字符串
         * 
         * @exception MainThreadException
         */
        fun getLogRecordText(): String {
            if (Looper.getMainLooper().isCurrentThread) 
                throw MainThreadException()
            
            synchronized(LogUtil::class) {
                var log: FoxLog? = lastLog
                val sb = StringBuilder()
                while (log != null) {
                    sb.append(log.toString())
                    log = log.preview
                }
                return sb.toString()
            }
        }
    }
}

/**
 * 异常捕捉回调
 */
fun interface OnExceptionCapturedListener {
    fun onCapture(isMainThread: Boolean, thread: Thread, e: Throwable)
}

/**
 * 日志数据类
 */
private data class FoxLog(
        val timeStamp: String,
        val level: String,
        val tag: String?,
        val msg: String?,
        val e: Throwable?
) {
    constructor(
            level: String,
            tag: String?,
            msg: String?,
            e: Throwable?
    ) : this(
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSS", Locale.CHINA).format(Date()),
            level, tag, msg, e
    )

    //单链表倒序
    var preview: FoxLog? = null

    override fun toString(): String {
        var log = "$timeStamp $level/$tag: $msg"
        if (e != null) log += ". throw -> ${e.stackTraceToString()}"
        log += "\n"
        return log
    }
}

private class MainThreadException: RuntimeException("方法 getLogRecordText 不能在主线程中运行")
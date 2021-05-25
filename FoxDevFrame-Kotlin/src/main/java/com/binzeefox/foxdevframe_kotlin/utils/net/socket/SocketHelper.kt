package com.binzeefox.foxdevframe_kotlin.utils.net.socket

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStream
import java.lang.ref.WeakReference
import java.net.Socket
import java.nio.charset.StandardCharsets
import java.util.concurrent.Executors

/**
 * Socket帮助类
 *
 * @author tong.xw
 * 2021/01/29 16:57
 */
class SocketHelper(private val socket: Socket) {
    private val instanceLock = Any()    // 实例锁
    private val loopExecutor = Executors.newSingleThreadExecutor()
    private val sendExecutor = Executors.newSingleThreadExecutor()

    private var sendStream: OutputStream? = null    // 发送流

    /**
     * 内部类，监听回调
     */
    companion object interface ListenCallback {
        fun onListen(socket: Socket, text: String)
        fun onDisconnect(socket: Socket)
        fun onError(e: Throwable)
    }

    /**
     * 是否连接
     */
    val isConnected: Boolean get() {
        synchronized(instanceLock) {
            return socket.isConnected
        }
    }

    /**
     * 开始监听
     */
    fun listen(callback: ListenCallback) {
        loopExecutor.execute(object : Runnable {
            val callbackRef = WeakReference(callback)

            override fun run() {
                try {
                    while (socket.isConnected) {
                        BufferedReader(InputStreamReader(socket.getInputStream())).let {
                            callbackRef.get()?.onListen(socket, it.readLine())
                        }
                    }
                } catch (e: Exception) {
                    callbackRef.get() ?: return
                    callbackRef.get()?.onError(e)
                    callbackRef.get()?.onDisconnect(socket)
                }
            }
        })
    }

    /**
     * 发送信息
     *
     * @param text 发送内容
     * @param callback 结果回调，参数0为是否成功，若有异常则放入参数1
     */
    fun send(text: String, callback: (Boolean, Throwable?) -> Unit) {
        sendExecutor.execute(object : Runnable {
            val callbackRef = WeakReference(callback)

            override fun run() {
                synchronized(socket) {
                    if (socket.isClosed) return
                    try {
                        sendStream = socket.getOutputStream()
                        sendStream?.write("${text}\n".toByteArray(StandardCharsets.UTF_8))
                        sendStream?.flush()
                        callbackRef.get()?.invoke(true, null)
                    } catch (e: Exception) {
                        callbackRef.get()?.invoke(false, e)
                    }
                }
            }
        })
    }

    /**
     * 关闭
     */
    fun close() {
        loopExecutor.shutdownNow()
        sendExecutor.shutdown()
        if (!socket.isClosed) socket.close()
    }
}
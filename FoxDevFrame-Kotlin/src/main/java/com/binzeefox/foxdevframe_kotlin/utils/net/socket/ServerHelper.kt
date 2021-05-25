package com.binzeefox.foxdevframe_kotlin.utils.net.socket

import android.Manifest
import androidx.annotation.RequiresPermission
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.Exception
import java.lang.RuntimeException
import java.lang.ref.WeakReference
import java.net.ServerSocket
import java.net.Socket
import java.nio.charset.StandardCharsets
import java.util.*
import java.util.concurrent.*


const val ERROR_CODE_PULSE_FAILED: Int = 0X01
const val ERROR_CODE_CLIENT_FAILED = 0X02
const val ERROR_CODE_SERVER_FAILED = 0X03
const val PULSE_PRE = "pulse//" //心跳包头

/**
 * 服务器回调
 */
interface SocketServerCallback {
    fun onAccept(client: Socket)
    fun onReceive(client: Socket, text: String)
    fun onLost(client: Socket)
    fun onError(errorCode: Int, t: Throwable)
}

/**
 * 心跳包拦截器，用于控制心跳包输出
 */
fun interface PulseInterceptor {
    fun onIntercept(client: Socket?): String
}

/**
 * Socket服务器帮助类
 *
 * @author tong.xw
 * 2021/01/29 17:20
 */
@Suppress("MemberVisibilityCanBePrivate")
class ServerHelper
@RequiresPermission(Manifest.permission.INTERNET)
constructor(
        val server: ServerSocket,
        var callback: SocketServerCallback? = null,
        var interceptor: PulseInterceptor = PulseInterceptor {
            return@PulseInterceptor PULSE_PRE + Date()
        }
) {

    private val instanceLock: Any = Any()   // 实例锁
    private val clientListenExecutor: ExecutorService = ThreadPoolExecutor(
            0, 50, 20, TimeUnit.SECONDS, LinkedBlockingQueue()
    ) // 最多监听50个客户端
    private val clientSendExecutor: ExecutorService = ThreadPoolExecutor(
            0, 16, 20, TimeUnit.SECONDS, LinkedBlockingQueue()
    ) // 发送数据，最高16线程
    private val workExecutor = Executors.newFixedThreadPool(3)  //工作线程池
    private val clientMap: ConcurrentHashMap<String, Socket> = ConcurrentHashMap()  //socket桶

    /**
     * 副构造器
     *
     * @param port 端口号
     */
    @RequiresPermission(Manifest.permission.INTERNET)
    constructor(
            port: Int,
            callback: SocketServerCallback? = null,
            interceptor: PulseInterceptor = PulseInterceptor {
                return@PulseInterceptor PULSE_PRE + Date()
            }
    ) : this(ServerSocket(port), callback, interceptor)

    /**
     * 开启服务器
     */
    fun open() {
        if (server.isClosed) return
        workExecutor.execute {
            while (!server.isClosed) {
                try {
                    val client = server.accept()
                    val address = client.remoteSocketAddress.toString()
                    if (clientMap.containsKey(address) && clientMap[address]?.isClosed == false)
                        continue
                    clientMap[address] = client
                    connectClient(client, address)
                } catch (e: IOException) {
                    onError(ERROR_CODE_SERVER_FAILED, e)
                }
            }
        }
        // 启动心跳包
        workExecutor.execute {
            try {
                while (!server.isClosed) {
                    pulse()
                    Thread.sleep(5000)
                }
            } catch (e: Exception) {
                onError(ERROR_CODE_PULSE_FAILED, e)
            }
        }
    }

    /**
     * 向所有客户端群发
     */
    fun broadcast(text: String) {
        for (client in clientMap.values)
            sendTo(client, text)
    }

    /**
     * 向地址发送
     */
    fun sendTo(address: String, text: String) {
        val client = clientMap[address]
        client?.also {
            sendTo(it, text)
        }
    }

    /**
     * 向客户端发送
     */
    fun sendTo(client: Socket, text: String) {
        synchronized(instanceLock) {
            if (client.isClosed) {
                clientMap.remove(client.remoteSocketAddress.toString())
                return
            }
            clientSendExecutor.execute(object : Runnable {
                val clientRef = WeakReference(client)

                override fun run() {
                    try {
                        val os = clientRef.get()?.getOutputStream()
                        os?.let {
                            it.write("${text}\n".toByteArray(StandardCharsets.UTF_8))
                            it.flush()
                        } ?: throw RuntimeException("客户端已断开")
                    } catch (e: Exception) {
                        lostClient(client)
                    }
                }
            })
        }
    }

    /**
     * 关闭服务器
     */
    fun close() {
        synchronized(instanceLock) {
            server.close()
            clientListenExecutor.shutdownNow()
            workExecutor.shutdownNow()
            clientMap.clear()
        }
    }

    /**
     * 是否已经关闭
     */
    val isClosed: Boolean
        get() {
            synchronized(instanceLock) {
                return server.isClosed
            }
        }

    ///////////////////////////////////////////////////////////////////////////
    // 内部方法
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 客户端失去连接
     */
    private fun lostClient(client: Socket) {
        val address = client.remoteSocketAddress.toString()
        client.close()
        clientMap.remove(address)
        callback?.onLost(client)
    }

    /**
     * 发送心跳包
     */
    private fun pulse() {
        for (client in clientMap.values) {
            val pulse = interceptor.onIntercept(client)
            sendTo(client, pulse)
        }
    }

    /**
     * 连接客户端
     */
    private fun connectClient(client: Socket, address: String) {
        synchronized(instanceLock) {
            callback?.onAccept(client)
            clientListenExecutor.execute {
                try {
                    BufferedReader(InputStreamReader(client.getInputStream())).use { br ->
                        while (!server.isClosed && !client.isClosed) {
                            val text = br.readLine()
                            if(text.isNullOrEmpty()) break
                            onMessageReceive(client, text)
                        }
                    }
                } catch (e: Exception) {
                    onError(ERROR_CODE_CLIENT_FAILED, e)
                } finally {
                    lostClient(client)
                }
            }
        }
    }

    /**
     * 收到消息
     */
    private fun onMessageReceive(client: Socket, text: String) {
        synchronized(instanceLock) {
            callback?.onReceive(client, text)
        }
    }

    /**
     * 错误信息
     */
    private fun onError(errorCode: Int, t: Throwable) {
        callback?.onError(errorCode, t)
    }
}
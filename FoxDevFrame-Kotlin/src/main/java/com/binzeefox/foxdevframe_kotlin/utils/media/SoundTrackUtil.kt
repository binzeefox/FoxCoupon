package com.binzeefox.foxdevframe_kotlin.utils.media

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.util.SparseIntArray
import androidx.annotation.RawRes
import androidx.core.util.isEmpty
import androidx.core.util.isNotEmpty
import com.binzeefox.foxdevframe_kotlin.FoxCore
import com.binzeefox.foxdevframe_kotlin.utils.net.LanScanner

/**
 * 声音播放工具
 *
 * 用于播放Raw文件内的声音
 * @author tong.xw
 * 2021/01/29 10:35
 */
class SoundTrackUtil(
        private val pool: SoundPool = SoundPool.Builder().setMaxStreams(1).build()
) {
    constructor(
            maxStream: Int = 1,
            attributes: AudioAttributes?
    ) : this(
            SoundPool.Builder().apply {
                setMaxStreams(maxStream)
                attributes?.let { setAudioAttributes(it) }
            }.build()
    )

    // 音频管理器
    private val manager: AudioManager
        get() =
            FoxCore.appContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    // 存放ID
    private val idArray = SparseIntArray()

    // 存放流ID
    private val streamArray = SparseIntArray()

    // 是否有声音在播放
    private val isInStream: Boolean
        get() {
            synchronized(streamArray) {
                return streamArray.isNotEmpty()
            }
        }

    // 音量
    private val volumeRatio: Float
        get() {
            // 获取最大音量值
            val audioMaxVolume: Float = manager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM).toFloat()
            // 不断获取当前的音量值
            val audioCurrentVolume: Float = manager.getStreamVolume(AudioManager.STREAM_SYSTEM).toFloat()
            //最终影响音量
            return Math.max(audioCurrentVolume / audioMaxVolume, 0.6f)
        }

    /**
     * 加载声音
     */
    fun loadSound(soundId: Int, priority: Int = 1): SoundTrackUtil = apply {
        idArray.append(soundId, pool.load(FoxCore.appContext, soundId, priority))
    }

    /**
     * 批量加载声音
     */
    fun loadSounds(soundIdList: List<Int>) {
        for (id in soundIdList) loadSound(id)
    }

    /**
     * 播放声音
     *
     * @param soundId 注册过的声音资源文件
     * @param volRatio 音量
     * @param priority 质量
     * @param loop    若0为不循环，-1为无限循环，其它数值为循环次数（播放次数等于循环次数+1）
     * @param rate 速率
     */
    fun play(
            @RawRes soundId: Int,
            volRatio: Float = volumeRatio,
            priority: Int = 1,
            loop: Int = 0,
            rate: Float = 1f
    ) {
        synchronized(streamArray) {
            val streamId = pool.play(idArray.get(soundId), volRatio, volRatio, priority, loop, rate)
            streamArray.put(soundId, streamId)
        }
    }

    /**
     * 停止播放全部声音
     */
    fun silence() {
        synchronized(streamArray) {
            for (i in 0..streamArray.size()) {
                pool.stop(streamArray.valueAt(i))
            }
            streamArray.clear()
        }
    }

    /**
     * 暂停全部声音
     */
    fun pauseAll() {
        synchronized(streamArray) {
            for (i in 0..streamArray.size()) {
                pool.pause(streamArray.valueAt(i))
            }
        }
    }

    /**
     * 恢复全部
     */
    fun resumeAll() {
        synchronized(streamArray) {
            for (i in 0..streamArray.size()) {
                pool.resume(streamArray.valueAt(i))
            }
        }
    }

    /**
     * 暂停播放
     */
    fun pause(@RawRes soundId: Int) {
        synchronized(streamArray) {
            val streamId = streamArray[soundId, 0]
            if (streamId != 0) pool.pause(streamId)
        }
    }

    /**
     * 停止播放
     */
    fun stop(@RawRes soundId: Int) {
        synchronized(streamArray) {
            val streamId = streamArray[soundId, 0]
            if (streamId != 0) {
                pool.stop(streamId)
                streamArray.removeAt(streamArray.indexOfKey(soundId))
            }
        }
    }
}
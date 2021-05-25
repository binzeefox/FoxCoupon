package com.binzeefox.foxdevframe_kotlin.utils

import java.security.MessageDigest
import kotlin.experimental.and

/**
 * MD5工具
 *
 * @author tong.xw
 * 2021/01/27 16:02
 */
object MD5Util {

    /**
     * 获取字符串的MD5摘要
     *
     * @param string 待摘要字符串
     * @return 字符串MD5码
     */
    fun md5(string: String): String {
        if (string.isEmpty()) return ""
        val md5 = MessageDigest.getInstance("MD5")
        val bytes = md5.digest(string.toByteArray())
        val sb = StringBuilder()
        for (b in bytes) {
            val temp = Integer.toHexString((b and 0xff.toByte()).toInt())
            if (temp.length == 1) sb.append(0)
            sb.append(temp)
        }
        return sb.toString()
    }
}
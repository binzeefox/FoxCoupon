package com.binzeefox.foxdevframe_kotlin.utils

import android.util.Patterns
import com.binzeefox.foxdevframe_kotlin.utils.TextTools.isObb
import java.util.*
import java.util.regex.Pattern
import kotlin.collections.HashMap

/**
 * 字符串工具
 *
 * @author tong.xw
 * 2021/01/27 16:09
 */
object TextTools {

    /**
     * 是否是整数
     */
    val String.isInteger: Boolean
        get() {
            return try {
                toLong()
                true
            } catch (e: Exception) {
                false
            }
        }

    /**
     * 是否是数字
     */
    val String.isNumber: Boolean
        get() {
            return try {
                toDouble()
                true
            } catch (e: Exception) {
                false
            }
        }

    /**
     * 是否是奇数
     */
    val String.isObb: Boolean
        get() {
            if (!isInteger) return false
            return toLong() % 2 == 1L
        }

    /**
     * 是否包含中文
     */
    val String.hasChinese: Boolean
        get() {
            if (isEmpty()) return false
            for (c in toCharArray()) {
                if (c.toInt() in 0x4E00..0x9FA5) return true
            }
            return false
        }

    /**
     * 是否是合法网络Url
     */
    val String.isWebUrl: Boolean
        get() = Patterns.WEB_URL.matcher(this).matches()

    /**
     * 是否是中华人民共和国居民身份证号
     */
    val String.isIDCardNum: Boolean
        get() {
            val pattern = Pattern.compile("\\d{15}(\\d{2}[0-9xX])?")
            return pattern.matcher(this).matches()
        }

    /**
     * 获取身份证信息
     * - 若非合法身份证号则返回null
     */
    val String.asIDCardNum: IDCardNum?
        get() {
            return if (!isIDCardNum) null
            else IDCardNum(this)
        }
}

/**
 * 身份证工具
 */
class IDCardNum(val cardNum: String) {
    private val cProvinceMap = HashMap<String, String>().apply {
        put("11", "北京")
        put("12", "天津")
        put("13", "河北")
        put("14", "山西")
        put("15", "内蒙古")
        put("21", "辽宁")
        put("22", "吉林")
        put("23", "黑龙江")
        put("31", "上海")
        put("32", "江苏")
        put("33", "浙江")
        put("34", "安徽")
        put("35", "福建")
        put("36", "江西")
        put("37", "山东")
        put("41", "河南")
        put("42", "湖北")
        put("43", "湖南")
        put("44", "广东")
        put("45", "广西")
        put("46", "海南")
        put("50", "重庆")
        put("51", "四川")
        put("52", "贵州")
        put("53", "云南")
        put("54", "西藏")
        put("61", "陕西")
        put("62", "甘肃")
        put("63", "青海")
        put("64", "宁夏")
        put("65", "新疆")
        put("71", "台湾")
        put("81", "香港")
        put("82", "澳门")
        put("91", "境外")
    }   //身份证地理字典

    // 省份
    val province: String get() = cProvinceMap[cardNum.substring(0, 2)] ?: "未知"

    // 生日
    val birthDay: Date
        get() {
            val birthday = cardNum.substring(6, 14)
            val year = Integer.parseInt(birthday.substring(0, 4))
            val month = Integer.parseInt(birthday.substring(4, 6))
            val day = Integer.parseInt(birthday.substring(6))

            val calendar = Calendar.getInstance(Locale.CHINA).apply {
                set(year, month, day)
            }

            return calendar.time
        }

    // 是否是男性
    val isMale: Boolean
        get() {
            val sex = cardNum.substring(cardNum.length - 2, cardNum.length - 1)
            return sex.isObb
        }
}
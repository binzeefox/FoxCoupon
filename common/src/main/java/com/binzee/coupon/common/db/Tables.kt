package com.binzee.coupon.common.db

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.*

/* 数据表 */

// 兑换券类别
const val COUPON_TYPE_CASH = 0
const val COUPON_TYPE_WISH = 1

/**
 * 兑换券
 * COUPON UID 规则: 字符串，"XX_${timeStamp}"。其中XX为CH表示现金，XX为WH表示愿望。timeStamp为创建时的时间戳
 */
@Entity(indices = [Index(value = ["couponUid"], unique = true)])
data class Coupon(
    @PrimaryKey(autoGenerate = true) var id: Long,  //主键
    val couponUid: String,  // 兑换券UID
    val type: Int,  //兑换券类别
    var title: String,  // 兑换券名称
    var describe: String?,   // 兑换券描述
    val createTime: Long = Date().time, // 创建时间
    var deadline: Long, //过期时间
    var depleteTime: Long = -1,   //使用的时间
    val amount: Int = 0    //金额（元￥）
)

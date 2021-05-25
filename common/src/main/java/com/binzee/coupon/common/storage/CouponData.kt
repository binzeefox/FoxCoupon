package com.binzee.coupon.common.storage

import android.os.Parcel
import android.os.Parcelable
import java.util.*

/**
 * 兑换券数据
 *
 * @author tong.xw
 * 2021/05/25 10:53
 */
data class CouponData(
    val couponUid: String,  // 兑换券UID
    var title: String,  // 兑换券名称
    var type: Int,  //兑换券类别
    var describe: String?,   // 兑换券描述
    val createTime: Long = Date().time, // 创建时间
    var deadline: Long, //过期时间
    var depleteTime: Long = -1,   //使用的时间
    val amount: Int = 0    //金额（元￥）
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readString(),
        parcel.readLong(),
        parcel.readLong(),
        parcel.readLong(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(couponUid)
        parcel.writeInt(type)
        parcel.writeString(title)
        parcel.writeString(describe)
        parcel.writeLong(createTime)
        parcel.writeLong(deadline)
        parcel.writeLong(depleteTime)
        parcel.writeInt(amount)
    }
    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CouponData> {
        override fun createFromParcel(parcel: Parcel): CouponData {
            return CouponData(parcel)
        }

        override fun newArray(size: Int): Array<CouponData?> {
            return arrayOfNulls(size)
        }
    }
}
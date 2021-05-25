package com.binzee.coupon.common.db

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * 兑换券数据库
 *
 * @author tong.xw
 * 2021/05/20 11:24
 */
@Database(entities = [Coupon::class], version = 1)
abstract class CouponDatabase: RoomDatabase() {
    abstract fun couponDao(): CouponDao
}
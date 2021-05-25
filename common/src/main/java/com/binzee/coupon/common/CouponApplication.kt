package com.binzee.coupon.common

import android.app.Application
import androidx.room.Room
import com.binzee.coupon.common.db.CouponDatabase
import com.binzeefox.foxdevframe_kotlin.FoxCore

/**
 * 应用
 *
 * @author tong.xw
 * 2021/05/20 11:05
 */
open class CouponApplication : Application() {

    companion object {
        private const val DB_NAME = "COUPON_DATABASE"

        /**
         * 数据库, 拓展到FoxCore
         */
        val FoxCore.couponDB: CouponDatabase by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            Room.databaseBuilder(FoxCore.appContext, CouponDatabase::class.java, DB_NAME)
                .allowMainThreadQueries()
                .build()
        }
    }
}
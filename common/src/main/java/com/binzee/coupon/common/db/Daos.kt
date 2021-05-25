package com.binzee.coupon.common.db

import androidx.room.*
import com.binzee.coupon.common.storage.CouponData

/* 数据操作 */

@Dao
interface CouponDao {

    /**
     * 添加全部
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE, entity = Coupon::class)
    fun insertAll(vararg data: CouponData): List<Long>


    /**
     * 添加
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE, entity = Coupon::class)
    fun insert(data: CouponData): Long

    /**
     * 删除
     */
    @Delete(entity = Coupon::class)
    fun delete(vararg coupon: Coupon)

    /**
     * 更新
     */
    @Update(onConflict = OnConflictStrategy.REPLACE, entity = Coupon::class)
    fun update(coupon: Coupon)

    /**
     * 查询全部
     */
    @Query("SELECT * FROM Coupon")
    fun queryAll(): List<Coupon>

    /**
     * 类别c查询
     */
    @Query("SELECT * FROM Coupon WHERE type = :type")
    fun queryByType(type: Int): List<Coupon>

    /**
     * Uid查询
     */
    @Query("SELECT * FROM Coupon WHERE couponUid = :couponUid")
    fun queryByUid(couponUid: String): Coupon?

    /**
     * 查询结束日期在时间区间内的数据
     */
    @Query("SELECT * From Coupon where deadLine >= :startTime and deadLine <= :endTime")
    fun queryByDeadlineRange(startTime: Long, endTime: Long): List<Coupon>

    /**
     * 查询创建日期在时间区间内的数据
     */
    @Query("SELECT * From Coupon where createTime >= :startTime and createTime <= :endTime")
    fun queryByCreateTimeRange(startTime: Long, endTime: Long): List<Coupon>

    /**
     * 查询所有可用兑换券(没有使用且没有过期)
     */
    @Query("SELECT * From Coupon where depleteTime = -1 and deadline < :currentTime")
    fun queryUsableCoupon(currentTime: Long): List<Coupon>
}
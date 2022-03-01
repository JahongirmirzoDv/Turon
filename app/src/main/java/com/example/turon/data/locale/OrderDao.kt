package com.example.turon.data.locale

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.turon.data.model.BrCode

@Dao
interface OrderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCodes(code: BrCode)

    @Query("SELECT * FROM codes_table WHERE orderId = :orderId")
    fun getAll(orderId:Int?): List<BrCode>

    @Query("DELETE FROM codes_table")
    fun deleteAllCodes()


    @Query("SELECT EXISTS (SELECT 1 FROM codes_table WHERE orderId = :orderId)")
    fun existsOrder(orderId: Int): Boolean

    @Query("SELECT EXISTS (SELECT 1 FROM codes_table WHERE code = :code)")
    fun exists(code: Long): Boolean
}
package com.example.turon.data.locale

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.turon.data.model.CodeScan

@Dao
interface CodeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFilter(code: CodeScan)

    @Query("select * from code_tables WHERE code =:code")
    fun getCode(code: Long): CodeScan

    @Query("SELECT * FROM code_tables WHERE productName = :productName")
    fun getAll(productName: String?): List<CodeScan>


    @Query("SELECT EXISTS (SELECT 1 FROM code_tables WHERE code = :code)")
    fun exists(code: Long): Boolean

    @Query("DELETE FROM code_tables")
    fun deleteFilterTable()




}
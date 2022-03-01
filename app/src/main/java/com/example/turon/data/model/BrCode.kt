package com.example.turon.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "codes_table")
class BrCode{
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
    var orderId: Int? =null
    @ColumnInfo(name = "code")
    var code: Long = 0
    @ColumnInfo(name = "productName")
    var productName: String = ""
}


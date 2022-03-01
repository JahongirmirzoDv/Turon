package com.example.turon.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "code_tables")
class CodeScan {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
    @ColumnInfo(name = "code")
    var code: Long = 0
    @ColumnInfo(name = "productName")
    var productName: String = ""
}


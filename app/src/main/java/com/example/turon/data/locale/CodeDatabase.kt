package com.example.turon.data.locale

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.turon.data.model.BrCode


@Database(entities = [BrCode::class], version = 1)
abstract class CodeDatabase : RoomDatabase() {

    abstract fun codeDao(): OrderDao

    companion object {

        @Volatile
        private var INSTANCE: CodeDatabase? = null

        var LOCK = Any()
        fun getRoomClient(context: Context): CodeDatabase {

            if (INSTANCE != null) return INSTANCE!!
            synchronized(LOCK) {
                INSTANCE = Room
                    .databaseBuilder(
                        context, CodeDatabase::class.java,
                        "codes.db"
                    )
                    .allowMainThreadQueries()
                    .build()
                return INSTANCE!!
            }
        }
    }
}
package com.example.turon.data.locale

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.turon.data.model.CodeScan


@Database(entities = [CodeScan::class], version = 1)
abstract class AppDatabase : RoomDatabase() {


    abstract fun codeDao(): CodeDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        var LOCK = Any()
        fun getRoomClient(context: Context) : AppDatabase {

            if (INSTANCE != null) return INSTANCE!!

            synchronized(LOCK) {


                INSTANCE = Room
                    .databaseBuilder(
                        context, AppDatabase::class.java,
                        "code.db"
                    )
                    .allowMainThreadQueries()
                    .build()

                return INSTANCE!!

            }
        }

    }

}
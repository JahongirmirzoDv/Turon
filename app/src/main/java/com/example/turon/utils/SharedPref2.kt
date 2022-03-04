package com.example.turon.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.core.content.edit

object SharedPref2 {
    lateinit var sharedPreferences: SharedPreferences

    fun getInstanceDis(context: Context) {
        sharedPreferences = context.getSharedPreferences(
            "" +
                    "", MODE_PRIVATE
        )
    }

    var user: Int?
        get() = sharedPreferences.getInt("user", 0)
        set(value) = sharedPreferences.edit {
            if (value != null) {
                this.putInt("user", value)
            }
        }
}
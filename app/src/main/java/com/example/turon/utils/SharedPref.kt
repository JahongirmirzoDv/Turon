package com.example.turon.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.example.turon.data.model.response.OrderData

class SharedPref(context: Context) {

    var device_token: String
        set(value) = mySharedPref.edit().putString("device_token", value).apply()
        get() = mySharedPref.getString("device_token", "")!!

    var brutto: String
        set(value) = mySharedPref.edit().putString("brutto", value).apply()
        get() = mySharedPref.getString("brutto", "")!!

    var tara: String
        set(value) = mySharedPref.edit().putString("tara", value).apply()
        get() = mySharedPref.getString("tara", "")!!


    var firebase_token: String
        set(value) = mySharedPref.edit().putString("firebase_token", value).apply()
        get() = mySharedPref.getString("firebase_token", "")!!

    var deviceDate: String
        set(value) = mySharedPref.edit().putString("device_date", value).apply()
        get() = mySharedPref.getString("device_date", "")!!

    private var mySharedPref: SharedPreferences =
        context.getSharedPreferences("preferences", Context.MODE_PRIVATE)


    fun setUserId(id: Int?) {
        val editor = mySharedPref.edit()
        editor.putInt("user_id", id!!)
        editor.apply()
    }

    fun getUserId(): Int {
        return mySharedPref.getInt("user_id", -1)
    }

    fun setUserType(type: String?) {
        val editor = mySharedPref.edit()
        editor.putString("user_type", type!!)
        editor.apply()
    }

    fun getUserType(): String {
        return mySharedPref.getString("user_type", "")!!
    }


    fun isFirstEnter(): Boolean {
        return mySharedPref.getBoolean("first_enter", true)
    }

    fun setFirstEnter(enter: Boolean?) {
        val editor = mySharedPref.edit()
        editor.putBoolean("first_enter", enter!!)
        editor.apply()
    }


    fun setTurnNumViloyat(turn_num: Int?) {
        val editor = mySharedPref.edit()
        editor.putInt("turn_numV", turn_num!!)
        editor.apply()
    }

    fun setTurnNumToshkent(turn_num: Int?) {
        val editor = mySharedPref.edit()
        editor.putInt("turn_numT", turn_num!!)
        editor.apply()
    }

    fun getTurnNumViloyat(): Int {
        return mySharedPref.getInt("turn_numV", 1)
    }

    fun getTurnNumToshkent(): Int {
        return mySharedPref.getInt("turn_numT", 1)
    }


    @SuppressLint("CommitPrefEdits")
    fun setClientData(data: OrderData) {
        val editor = mySharedPref.edit()
        editor.putInt("id", data.id)
        editor.putString("date", data.date)
        editor.putString("phoneNum", data.phone)
        editor.putString("carNum", data.carNum)
        editor.putString("client", data.client)
        editor.putString("status", data.status)
        editor.apply()
    }

    fun getClientData(): OrderData {
        return OrderData(
            mySharedPref.getInt("id", -1),
            mySharedPref.getString("client", "")!!,
            mySharedPref.getString("date", "")!!,
            mySharedPref.getString("carNum", "")!!,
            mySharedPref.getString("phoneNum", "")!!,
            mySharedPref.getString("status", "")!!
        )
    }
}
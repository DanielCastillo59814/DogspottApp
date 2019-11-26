package com.a000webhostapp.dogspott.dogspottapp.utilities

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson

class Properties {
    companion object {
        const val USER_KEY = "userkey"

        const val DEFAULT_LOCATION:String = "dogspottstorage"

        fun getProperty(c:Context, k:String):String{
            val p:SharedPreferences = c.getSharedPreferences(DEFAULT_LOCATION,Context.MODE_PRIVATE)
            val a = p.getString(k,"")!!
            return a
        }
        fun setProperty(c:Context, k:String, v:String){
            val p:SharedPreferences = c.getSharedPreferences(DEFAULT_LOCATION,Context.MODE_PRIVATE)
            val e:SharedPreferences.Editor = p.edit()
            e.putString(k,v)
            e.apply()
        }
        fun containsProperty(c:Context, k:String):Boolean{
            return c.getSharedPreferences(DEFAULT_LOCATION,Context.MODE_PRIVATE).contains(k)
        }
        fun removeProperty(c:Context,k:String){
            val p:SharedPreferences = c.getSharedPreferences(DEFAULT_LOCATION,Context.MODE_PRIVATE)
            val e:SharedPreferences.Editor = p.edit()
            e.remove(k)
            e.apply()
        }
        fun clearProperties(c:Context){
            val p:SharedPreferences = c.getSharedPreferences(DEFAULT_LOCATION,Context.MODE_PRIVATE)
            val e:SharedPreferences.Editor = p.edit()
            e.clear()
            e.apply()
        }
    }
}
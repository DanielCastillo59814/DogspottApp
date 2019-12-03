package com.a000webhostapp.dogspott.dogspottapp.utilities

import android.content.Context
import android.content.SharedPreferences

/**
 * Clase que maneja la persistencia en Android
 */
class Properties {
    companion object {
        // Llave para el key obtenido por la API
        const val USER_KEY = "userkey"
        // ubicación de las propiedades en persistencia
        const val DEFAULT_LOCATION:String = "dogspottstorage"

        /**
         * Obtiene una propiedad guardada
         * @param context el contexto de la aplicación
         * @param key la llame asociada a la propiedad
         * @return la propiedad guardada
         */
        fun getProperty(context:Context, key:String):String{
            val p:SharedPreferences = context.getSharedPreferences(DEFAULT_LOCATION,Context.MODE_PRIVATE)
            val value = p.getString(key,"")!!
            return value
        }

        /**
         * Guarda una propiedad
         * @param context el contexto de la aplicación
         * @param key la llave a asociar a la propiedad
         * @param value la propiedad a guardar
         */
        fun setProperty(context:Context, key:String, value:String){
            val p:SharedPreferences = context.getSharedPreferences(DEFAULT_LOCATION,Context.MODE_PRIVATE)
            val e:SharedPreferences.Editor = p.edit()
            e.putString(key,value)
            e.apply()
        }

        /**
         * Verifica si una propiedad ya ha sido guardada
         * @param context el contexto de la aplicación
         * @param key la llave de la propiedad a verificar
         * @return True si la llave ya se encuentra ocupada, False en otro caso
         */
        fun containsProperty(context:Context, key:String):Boolean{
            return context.getSharedPreferences(DEFAULT_LOCATION,Context.MODE_PRIVATE).contains(key)
        }

        /**
         * Remueve una propiedad guardada
         * @param context el contexto de la aplicación
         * @param key la llave de la propiedad a remover
         */
        fun removeProperty(context:Context,key:String){
            val p:SharedPreferences = context.getSharedPreferences(DEFAULT_LOCATION,Context.MODE_PRIVATE)
            val e:SharedPreferences.Editor = p.edit()
            e.remove(key)
            e.apply()
        }

        /**
         * Limpia todas las propiedades
         * @param context el contexto de la aplicación
         */
        fun clearProperties(context:Context){
            val p:SharedPreferences = context.getSharedPreferences(DEFAULT_LOCATION,Context.MODE_PRIVATE)
            val e:SharedPreferences.Editor = p.edit()
            e.clear()
            e.apply()
        }
    }
}
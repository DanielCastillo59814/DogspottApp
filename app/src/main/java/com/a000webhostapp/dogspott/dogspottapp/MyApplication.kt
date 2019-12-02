package com.a000webhostapp.dogspott.dogspottapp

import android.app.Application
import com.a000webhostapp.dogspott.dogspottapp.retro.ConnectionAdapter

/**
 * Clase que representa la aplicación android
 */
class MyApplication: Application() {
    /**
     * Primer método de la aplicación. Aquí instanciamos el servicio
     * que realiza llamadas a la API
     */
    override fun onCreate() {
        super.onCreate()
        ConnectionAdapter.initService(this)
    }
}
package com.a000webhostapp.dogspott.dogspottapp

import android.app.Application
import com.a000webhostapp.dogspott.dogspottapp.retro.ConnectionAdapter

class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        ConnectionAdapter.initService(this)
    }
}
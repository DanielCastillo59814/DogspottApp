package com.a000webhostapp.dogspott.dogspottapp.utilities

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.google.android.material.snackbar.Snackbar
import android.view.View
import com.a000webhostapp.dogspott.dogspottapp.R

class State {
    companion object {
        fun isNetworkAvailable(context: Context, view: View? = null, retry:(() -> Unit)? = null, onFalse:(() -> Unit)? = null):Boolean{
            val cn: ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val info: NetworkInfo? = cn.activeNetworkInfo
            val connected = info?.isConnected ?: false
            if(!connected){
                var snack: Snackbar? = null
                view?.let{snack = Snackbar.make(it, R.string.log_network_unavailable, if(retry != null) Snackbar.LENGTH_INDEFINITE else Snackbar.LENGTH_LONG)}
                retry?.let{
                    snack?.setAction(R.string.action_again, {it()})
                }
                snack?.show()
                onFalse?.invoke()
            }
            return connected
        }
    }
}
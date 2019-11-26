package com.a000webhostapp.dogspott.dogspottapp.retro

import android.content.Context
import com.google.android.material.snackbar.Snackbar
import android.util.Log
import android.view.View
import com.a000webhostapp.dogspott.dogspottapp.R
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class ConnectionAdapter {
    companion object {
        const val TAG = "ConnectionAdapter"
        lateinit var service:ConnectionService
        fun initService(context: Context){
            val builder = OkHttpClient.Builder()
                    .addNetworkInterceptor(HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    })
            service = Retrofit.Builder()
                    .baseUrl(context.getString(R.string.url_api_base))
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(builder.build())
                    .build()
                    .create(ConnectionService::class.java)
        }
        fun handleError(error: Throwable, rootView: View? = null, doSomethingElse:(() -> Unit)? = null, retry:(() -> Unit)? = null){
            Log.e(TAG,"error: ",error)
            var snack: Snackbar? = null
            if(rootView != null)snack = Snackbar.make(rootView, error.message ?: rootView.context.getString(R.string.log_server_error), if(retry != null) Snackbar.LENGTH_INDEFINITE else Snackbar.LENGTH_LONG)
            retry?.let{
                snack?.setAction(R.string.action_again) {it()}
            }
            snack?.show()
            doSomethingElse?.invoke()
        }
    }
}
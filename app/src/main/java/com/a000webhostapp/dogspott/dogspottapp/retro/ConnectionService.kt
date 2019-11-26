package com.a000webhostapp.dogspott.dogspottapp.retro

import com.a000webhostapp.dogspott.dogspottapp.retro.models.Dog
import io.reactivex.Observable
import retrofit2.http.*

interface ConnectionService {
    @GET("index.php")
    fun getFeed(@Query("key")key: String)
    :Observable<List<Dog>>
}
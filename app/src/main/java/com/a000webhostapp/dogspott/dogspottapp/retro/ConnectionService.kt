package com.a000webhostapp.dogspott.dogspottapp.retro

import com.a000webhostapp.dogspott.dogspottapp.retro.models.Dog
import com.a000webhostapp.dogspott.dogspottapp.retro.models.SimpleResponse
import io.reactivex.Observable
import retrofit2.http.*

interface ConnectionService {
    @GET("index.php")
    fun getFeed(@Query("key")key: String)
    :Observable<List<Dog>>

    @GET("signup.php")
    fun signup(@Query("username")username: String,
               @Query("password")password: String)
    :Observable<SimpleResponse>

    @GET("like.php")
    fun like(@Query("key")key: String,
             @Query("dog_id")dog_id: String)
    :Observable<SimpleResponse>
}
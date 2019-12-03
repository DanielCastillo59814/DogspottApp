package com.a000webhostapp.dogspott.dogspottapp.retro

import com.a000webhostapp.dogspott.dogspottapp.retro.models.Dog
import com.a000webhostapp.dogspott.dogspottapp.retro.models.SimpleResponse
import io.reactivex.Observable
import retrofit2.http.*

/**
 * Servicio que hace peticiones a la API
 */
interface ConnectionService {
    /**
     * Obtiene el feed de perros
     * @param key la clave de autenticación
     * @return una lista de perros
     */
    @GET("index.php")
    fun getFeed(@Query("key")key: String)
    :Observable<List<Dog>>

    /**
     * Registra un nuevo usuario
     * @param username el nombre del usuario
     * @param password la contraseña del usuario
     * @return una respuesta simple de la API
     */
    @GET("signup.php")
    fun signup(@Query("username")username: String,
               @Query("password")password: String)
    :Observable<SimpleResponse>

    /**
     * Aumenta los likes de un perro
     * @param key la clave de autenticación
     * @param dog_id el ID del perro a likear
     * @return una respuesta simple de la API
     */
    @GET("like.php")
    fun like(@Query("key")key: String,
             @Query("dog_id")dog_id: String)
    :Observable<SimpleResponse>
}
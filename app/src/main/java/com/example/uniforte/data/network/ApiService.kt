package com.example.uniforte.data.network // Ajuste o pacote

import okhttp3.ResponseBody // Importar ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Call

interface ApiService {

    @POST("api/usuarios/login" ) // Endpoint relativo à BASE_URL
    suspend fun login(@Body requestBody: Map<String, String>): Response<ResponseBody> // Usar Map e ResponseBody


    @POST("api/usuarios/")
    fun cadastrarUsuario(@Body requestBody: Map<String, String>): Call <ResponseBody> //Retorna Call
}

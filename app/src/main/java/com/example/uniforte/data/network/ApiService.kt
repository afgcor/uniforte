package com.example.uniforte.data.network

import com.example.uniforte.data.network.UpdateUsuarioRequest
import com.example.uniforte.data.network.Usuario
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {

    @POST("api/usuarios/login")
    suspend fun login(@Body requestBody: Map<String, String>): Response<ResponseBody> // Usar Map e ResponseBody

    @POST("api/usuarios/")
    fun cadastrarUsuario(@Body requestBody: Map<String, String>): Call<ResponseBody> // Retorna Call

    @GET("api/usuarios/{id}")
    suspend fun buscarUsuarioPorId(@Path("id") userId: String): Response<Usuario>

    @PUT("api/usuarios/{id}")
    suspend fun atualizarUsuario(
        @Path("id") userId: String,
        @Body dadosAtualizacao: UpdateUsuarioRequest
    ): Response<Usuario>

}


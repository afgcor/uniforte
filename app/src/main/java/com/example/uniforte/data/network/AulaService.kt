package com.example.uniforte.data.network




import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.GET
import com.example.uniforte.data.model.AulaProfessor

interface AulaService {
    @POST("adicionar_aula")
    fun adicionarAula(
        @Body aula: AulaProfessor,
        @Header("apikey") apiKey: String,
        @Header("Authorization") auth: String
    ): Call<Void>
}
package com.example.uniforte.data.network

import com.example.uniforte.data.model.FichaTreino
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @POST("api/usuarios/login"  )
    suspend fun login(@Body requestBody: Map<String, String>): Response<ResponseBody>

    @POST("api/usuarios/")
    fun cadastrarUsuario(@Body requestBody: Map<String, String>): Call<ResponseBody>

    @GET("api/usuarios/{id}")
    suspend fun buscarUsuarioPorId(@Path("id") userId: String): Response<Usuario>

    @PUT("api/usuarios/{id}")
    suspend fun atualizarUsuario(
        @Path("id") userId: String,
        @Body dadosAtualizacao: UpdateUsuarioRequest
    ): Response<Usuario>

    @GET("api/fichas/aluno/{alunoId}") // Ajuste este caminho se sua rota for diferente
    suspend fun getFichasTreinoByAlunoId(@Path("alunoId") alunoId: String): Response<List<FichaTreino>>

    @GET("api/exercicios-ficha/{usuario_id}")
    suspend fun getFichasTreinoByUserId(@Path("usuario_id") userId: String): Response<FichaTreino>

    @GET("api/aulas/professor/{professor_id}")
    suspend fun getAulasByProfessorId(@Path("professor_id") professorId: String): Response<ResponseBody>

    @GET("api/fichas/aluno/{alunoId}") // Rota baseada em fichaTreinoRoutes.js [cite: 30]
    suspend fun getFichasTreinoByUserId(
        @Path("alunoId") userId: String,
        @Header("Authorization") token: String? = null // Inclua o token se sua API exigir autenticação
    ): Response<List<FichaTreino>> // Retorna uma lista, pois listarFichasTreinoPorAlunoId retorna uma lista [cite: 50]

    @GET("api/fichas/{id}") // Importante: rota /api/fichas/:id
    suspend fun getFichaTreinoById(@Path("id") id: Int): Response<FichaTreino>
}


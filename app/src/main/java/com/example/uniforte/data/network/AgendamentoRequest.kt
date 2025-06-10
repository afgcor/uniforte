package com.example.uniforte.data.network

import com.google.gson.annotations.SerializedName

data class AgendamentoRequest(
    @SerializedName("aula_id")
    val aulaId: Int,
    @SerializedName("aluno_id")
    val alunoId: String,
    @SerializedName("nome")
    val nome: String,
    @SerializedName("data")
    val data: String,
    @SerializedName("descricao")
    val descricao: String,
    @SerializedName("horario")
    val horario: String
)
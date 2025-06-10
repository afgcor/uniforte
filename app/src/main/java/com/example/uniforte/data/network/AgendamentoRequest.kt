// src/main/java/com/example/uniforte/data/network/AgendamentoRequest.kt
package com.example.uniforte.data.network

import com.google.gson.annotations.SerializedName

data class AgendamentoRequest(
    @SerializedName("aula_id")
    val aulaId: Int,
    @SerializedName("aluno_id")
    val alunoId: String,
    @SerializedName("nome") // Assuming this is the name of the class/appointment
    val nome: String,
    @SerializedName("data")
    val data: String,
    @SerializedName("descricao") // Assuming this is the description of the class/appointment
    val descricao: String,
    @SerializedName("horario")
    val horario: String
)
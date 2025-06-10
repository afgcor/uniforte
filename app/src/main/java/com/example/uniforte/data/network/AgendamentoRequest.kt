package com.example.uniforte.data.network

import com.google.gson.annotations.SerializedName // Importe esta anotação

data class AgendamentoRequest(
    @SerializedName("aula_id") // Mapeia 'aulaId' para 'aula_id' no JSON
    val aulaId: Int, // Pode manter 'aulaId' no Kotlin se preferir
    // Se o 'aluno_id' na sua classe for 'alunoId' (camelCase) como no seu SharedPreferences, você precisaria adicionar:
    // @SerializedName("aluno_id")
    // val alunoId: String?,
    // Mas se já estiver 'aluno_id' na sua data class, não precisa da anotação
    val aluno_id: String?, // Se esta é a declaração atual na sua data class, está ok.
    val nome: String,
    val data: String,
    val descricao: String,
    val horario: String
)
package com.example.uniforte.data.model

data class Agendamento(
    val id: Int,
    val aulaId: Int,
    val alunoId: String,
    val nome: String,
    val data: String,
    val descricao: String,
    val horario: String
)
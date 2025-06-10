package com.example.uniforte.models

import kotlinx.serialization.Serializable

@Serializable
data class Aula(
    val id: String,
    val titulo: String,
    val descricao: String,
    val data: String,
    val horario: String
) 
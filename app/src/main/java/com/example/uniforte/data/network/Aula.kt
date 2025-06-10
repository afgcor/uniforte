package com.example.uniforte.data

import com.google.gson.annotations.SerializedName

data class Aula(
    val id: String,
    val nome: String,
    val descricao: String,
    val horario: String,
    val data: String,
    val professor: Professor
)

data class Professor(
    @SerializedName("id_usuario")
    val idUsuario: String,
    val usuarios: Usuario
)

data class Usuario(
    val nome: String
)


package com.example.uniforte.data.model

import com.google.gson.annotations.SerializedName

data class FichaTreino(
    @SerializedName("id") val id: String,
    @SerializedName("titulo") val titulo: String,
    @SerializedName("descricao") val descricao: String,
    @SerializedName("exercicios") val exercicios: List<Exercicio>,
    @SerializedName("nomeProfessor") val nomeProfessor: String
)

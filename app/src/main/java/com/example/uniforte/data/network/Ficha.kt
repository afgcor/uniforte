// src/main/java/com/example/uniforte/data/network/Ficha.kt
package com.example.uniforte.data.network

data class Ficha(
    val id: Int,
    val alunoId: String,
    val professorId: String,
    val objetivo: String,
    val nomeProfessor: String,
    val exercicios: List<Exercicio>
)

data class Exercicio(
    val id: Int,
    val nome: String,
    val maquina: String,
    val series: Int,
    val repeticoes: Int,
    val carga: String?,
    val observacoes: String?
)
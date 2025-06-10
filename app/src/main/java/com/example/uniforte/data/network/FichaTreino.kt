package com.example.uniforte.data.model

data class FichaTreino(
    val id: Int?,
    val alunoId: String?,
    val professorId: String?,
    val dataCriacao: String?,
    val objetivo: String?,
    val nomeProfessor: String?,
    val exercicios: List<ExercicioFichaDetalhes>?
)

data class ExercicioFichaDetalhes(
    val id: Int?,
    val nome: String?,
    val maquina: String?,
    val series: Int?,
    val repeticoes: Int?,
    val carga: String?,
    val observacoes: String?
)
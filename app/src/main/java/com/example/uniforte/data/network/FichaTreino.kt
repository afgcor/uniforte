// com.example.uniforte.data.model.FichaTreino.kt
package com.example.uniforte.data.model

// Remova estas importações se não estiverem sendo usadas por mais nada
// import android.os.Parcelable
// import kotlinx.parcelize.Parcelize

// Remova a anotação @Parcelize e a herança de Parcelable
data class FichaTreino(
    val id: Int?,
    val alunoId: String?,      // Corrigido para String? (UUID)
    val professorId: String?,  // Corrigido para String? (UUID)
    val dataCriacao: String?,
    val objetivo: String?,
    val nomeProfessor: String?,
    val exercicios: List<ExercicioFichaDetalhes>?
)

// Remova a anotação @Parcelize e a herança de Parcelable
data class ExercicioFichaDetalhes(
    val id: Int?,
    val nome: String?,
    val maquina: String?,
    val series: Int?,
    val repeticoes: Int?,
    val carga: String?,        // Corrigido para String?
    val observacoes: String?
)
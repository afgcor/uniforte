// com.example.uniforte.data.model.ExercicioFichaItem.kt
package com.example.uniforte.data.model

import com.google.gson.annotations.SerializedName

// Esta classe representa um item dentro da lista 'exercicios' de uma FichaTreino
// Ela contém o objeto 'exercicio' real, que tem nome, descricao, series, repeticoes.
data class ExercicioFichaItem(
    @SerializedName("exercicio") val exercicio: Exercicio? // O objeto Exercicio real
)


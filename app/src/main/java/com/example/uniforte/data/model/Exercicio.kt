package com.example.uniforte.data.model

data class Exercicio(
    val id: String,
    val nome: String,
    val descricao: String?,
    val series: String?,      // <--- Resolvendo o erro de "series" e "repeticoes"
    val repeticoes: String?   // <--- Certifique-se de que o tipo corresponde ao DB, ex: Int ou String
)
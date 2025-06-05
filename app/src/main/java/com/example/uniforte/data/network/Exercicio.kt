package com.example.uniforte.data.model

import com.google.gson.annotations.SerializedName

data class Exercicio(
    @SerializedName("id") val id: String? = null,
    @SerializedName("created_at") val created_at: String? = null,
    @SerializedName("nome") val nome: String,
    @SerializedName("maquina") val maquina: String? = null
)

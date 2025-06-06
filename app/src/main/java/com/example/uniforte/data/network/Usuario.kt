package com.example.uniforte.data.network

import com.google.gson.annotations.SerializedName


data class Usuario(
    @SerializedName("id")
    val id: String? = null,

    @SerializedName("nome")
    val nome: String? = null,

    @SerializedName("cpf")
    val cpf: String? = null,

    @SerializedName("email")
    val email: String? = null,

    @SerializedName("telefone")
    val telefone: String? = null,

    @SerializedName("endereco")
    val endereco: String? = null,

    @SerializedName("titulo_objetivo")
    val titulo_objetivo: String?,

    @SerializedName("descricao_objetivo")
    val descricao_objetivo: String?
)

data class UpdateUsuarioRequest(
    @SerializedName("nome")
    val nome: String? = null,

    @SerializedName("email")
    val email: String? = null,

    @SerializedName("telefone")
    val telefone: String? = null,

    @SerializedName("endereco")
    val endereco: String? = null,

//    @SerializedName("titulo_objetivo")
//    val titulo_objetivo: String?,
//
//    @SerializedName("descricao_objetivo")
//    val descricao_objetivo: String?
)

package com.example.uniforte.data.network // Ajuste o nome do pacote se necessário

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private const val BASE_URL = "http://192.168.1.3:3000/" // <-- MUDE AQUI SE NECESSÁRIO

    private val retrofit by lazy {
        Retrofit.Builder( )
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()) // Necessário mesmo sem modelos explícitos
            .build()
    }

    val api: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}

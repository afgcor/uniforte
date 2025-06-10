// com.example.uniforte.api.RetrofitClient.kt
package com.example.uniforte.data.network



import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // ATENÇÃO: SUBSTITUA PELA URL REAL DO SEU SERVIDOR NODE.JS
    // Por exemplo: "http://192.168.1.100:3000/" se estiver na sua rede local
    // Use o IP da sua máquina ou o localhost do emulador (10.0.2.2)
    private const val BASE_URL = "http://192.168.0.7:3000/"

    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
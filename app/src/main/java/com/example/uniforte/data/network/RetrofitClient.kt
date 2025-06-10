package com.example.uniforte.data.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor // Import the logging interceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit // Import for timeouts if you want to add them

object RetrofitClient {
    // ATENÇÃO: SUBSTITUA PELA URL REAL DO SEU SERVIDOR NODE.JS
    // Por exemplo: "http://192.168.1.100:3000/" se estiver na sua rede local
    // Use o IP da sua máquina ou o localhost do emulador (10.0.2.2)
    private const val BASE_URL = "http://192.168.18.97:3000/"

    // 1. Create a logging interceptor
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        // Set the desired logging level
        // BODY: Logs request and response headers and bodies. Useful for debugging.
        // HEADERS: Logs headers only.
        // BASIC: Logs request and response lines.
        // NONE: No logging.
        setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    // 2. Create an OkHttpClient and add the logging interceptor
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        // Optional: Add timeouts for robustness
        .connectTimeout(30, TimeUnit.SECONDS) // Connect timeout
        .readTimeout(30, TimeUnit.SECONDS)    // Read timeout
        .writeTimeout(30, TimeUnit.SECONDS)   // Write timeout
        .build()

    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient) // 3. Set the custom OkHttpClient here
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
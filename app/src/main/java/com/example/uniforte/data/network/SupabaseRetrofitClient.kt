package com.example.uniforte.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object SupabaseRetrofitClient {

    private const val BASE_URL = "https://fiukdbdtmkskqyehcrkj.supabase.co/rest/v1/"  // <-- Substitua pela sua URL Supabase

    val instance: AulaService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AulaService::class.java)
    }
}
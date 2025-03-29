package com.example.fetchcodingexercise

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Singleton API instance.
// It's lazy, meaning it's only initialized when first used.
object RetrofitClient {
    val api: RetrofitAPI by lazy {
        Retrofit.Builder()
            // set fetch location
            .baseUrl("https://fetch-hiring.s3.amazonaws.com/")
            // convert JSON entries to Item instances
            .addConverterFactory(GsonConverterFactory.create())
            // generate a Retrofit instance with the given specifications
            .build()
            // generate a proxy class from the API
            .create(RetrofitAPI::class.java)
    }
}
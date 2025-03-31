package com.example.fetchcodingexercise

import retrofit2.http.GET

// Where web requests are sent and received.
// For this app, we only need one function, which is reading all the entries.
interface RetrofitAPI {
    @GET("hiring.json")
    suspend fun getItems(): List<Item>
}
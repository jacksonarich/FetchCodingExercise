package com.example.fetchcodingexercise

// This is my model for the dataset provided.
// Each row corresponds to an instance of this class.
data class Item(
    val id: Int,
    val listId: Int,
    val name: String?
)
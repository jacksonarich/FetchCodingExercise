package com.example.fetchcodingexercise

// This is my model for each row in the dataset.
// Each row corresponds to an instance of this class.
data class Item(
    val id: Int,
    val listId: Int,
    val name: String?
)
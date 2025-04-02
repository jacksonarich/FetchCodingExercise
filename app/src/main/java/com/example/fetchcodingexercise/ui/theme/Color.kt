package com.example.fetchcodingexercise.ui.theme

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat

// retrieve colors from res/values/colors.xml
fun getXmlColor(context: Context, colorResId: Int): Color {
    return Color(ContextCompat.getColor(context, colorResId))
}

val PurpleAccent = Color(0x60b041d9)
package com.example.fetchcodingexercise.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.example.fetchcodingexercise.R

@Composable
fun FetchCodingExerciseTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current

    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> darkColorScheme(
            primary = Color.DarkGray,
            secondary = Color.Blue,
            tertiary = Color.Blue,
            background = Color.Black,
            surface = PurpleAccent,
            onPrimary = Color.Blue,
            onSecondary = Color.Blue,
            onTertiary = Color.Blue,
            onBackground = Color.Blue,
            onSurface = Color.White,
        )
        else -> lightColorScheme(
            primary = Color.LightGray,
            secondary = Color.Blue,
            tertiary = Color.Blue,
            background = Color.White,
            surface = PurpleAccent,
            onPrimary = Color.Blue,
            onSecondary = Color.Blue,
            onTertiary = Color.Blue,
            onBackground = Color.Blue,
            onSurface = Color.Black,
        )
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
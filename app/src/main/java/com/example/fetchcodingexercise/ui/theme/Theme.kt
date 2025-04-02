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
            primary = Color.White,
            secondary = Color.Red,
            tertiary = Color.Red,
            background = Color.Black,
            surface = PurpleAccent,
            onPrimary = Color.Red,
            onSecondary = Color.Red,
            onTertiary = Color.Red,
            onBackground = Color.Red,
            onSurface = Color.White,
        )
        else -> lightColorScheme(
            primary = Color.Black,
            secondary = Color.Red,
            tertiary = Color.Red,
            background = Color.White,
            surface = PurpleAccent,
            onPrimary = Color.Red,
            onSecondary = Color.Red,
            onTertiary = Color.Red,
            onBackground = Color.Red,
            onSurface = Color.Black,
        )
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
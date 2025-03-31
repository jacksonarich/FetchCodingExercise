package com.example.fetchcodingexercise

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fetchcodingexercise.ui.theme.FetchCodingExerciseTheme

// This is the first screen the user sees after the app launches.
class MainActivity : ComponentActivity() {
    private val viewModel: ItemsViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // initialize UI
        enableEdgeToEdge()
        setContent {
            FetchCodingExerciseTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(modifier = Modifier.padding(innerPadding))
                }
            }
        }
        // fetch data
        viewModel.fetchItems()
        // observe state changes

    }
}

@Composable
fun Greeting(modifier: Modifier = Modifier) {
    Box(modifier = Modifier
        .fillMaxSize()
        .wrapContentSize(Alignment.Center)) {
        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier.background(color = Color.Red),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TitleField(modifier = modifier)
            NotesField(modifier = modifier)
        }
    }
}

@Composable
fun TitleField(modifier: Modifier = Modifier) {
    var text by rememberSaveable { mutableStateOf("Hello") }
    BasicTextField(
        value = text,
        onValueChange = { text = it },
        textStyle = TextStyle(
            fontSize = 40.sp,
            textAlign = TextAlign.Center),
        modifier = modifier
    )
}

@Composable
fun NotesField(modifier: Modifier = Modifier) {
    var text by rememberSaveable { mutableStateOf("World") }
    BasicTextField(
        value = text,
        onValueChange = { text = it },
        textStyle = TextStyle(
            fontSize = 20.sp,
            textAlign = TextAlign.Center),
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FetchCodingExerciseTheme {
        Greeting()
    }
}
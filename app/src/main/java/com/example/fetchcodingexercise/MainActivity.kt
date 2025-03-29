package com.example.fetchcodingexercise

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale

// activity = screen
// fragment = activity module

// This is the first screen the user sees after the app launches.
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FetchCodingExerciseTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(modifier = Modifier.padding(innerPadding))
                }
            }
        }
        fetchData()
    }

    private fun getPercent(numerator: Int, denominator: Int): String {
        val percent = numerator.toDouble() / denominator * 100
        val percentStr = String.format(Locale.getDefault(), "%.0f", percent)
        return "($percentStr%)"
    }

    private fun fetchData() {
        RetrofitClient.api.getItems().enqueue(object : Callback<List<Item>> {
            override fun onResponse(call: Call<List<Item>>, response: Response<List<Item>>) {
                if (!response.isSuccessful) {
                    Log.e("DEBUG", "Error: ${response.code()}")
                    return
                }
                // get the raw data, an unsorted list of all entries
                val allItems = (response.body() ?: emptyList())
                // filter items with no name, keeping track of how many items were removed
                val numItems1 = allItems.count()
                Log.d("DEBUG", "Received $numItems1 items")
                val filteredItems = allItems.filter { !it.name.isNullOrEmpty() }
                val numItems2 = filteredItems.count()
                val numRemoved = numItems1 - numItems2
                val percentRemoved = getPercent(numRemoved, numItems1)
                Log.d("DEBUG", "Filtered out $numRemoved unnamed items $percentRemoved")
                // reformat
                val itemLists = filteredItems
                    .groupBy { it.listId }
                    .mapValues { (_, items) -> items.sortedBy { it.name } }
                    .toSortedMap()
                // TODO: set UI

                // verify all items have matching names and IDs
                // TODO: do this on a different thread
                var numMismatch = 0
                for (item in filteredItems) {
                    val actualName = item.name
                    val expectedName = "Item ${item.id}"
                    if (expectedName != actualName) {
                        numMismatch += 1
                    }
                }
                if (numMismatch == 1) {
                    Log.d("DEBUG", "Verified all items follow the naming convention")
                } else {
                    val percentMismatch = getPercent(numMismatch, numItems2)
                    Log.e("DEBUG", "$numMismatch items don't follow the naming convention $percentMismatch")
                }
            }

            override fun onFailure(call: Call<List<Item>>, t: Throwable) {
                Log.e("FetchData", "Failed: ${t.message}")
            }
        })
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
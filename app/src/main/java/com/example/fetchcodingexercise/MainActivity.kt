package com.example.fetchcodingexercise

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fetchcodingexercise.ui.theme.FetchCodingExerciseTheme

// This is the first screen the user sees after the app launches.
class MainActivity : ComponentActivity() {
    private val viewModel: ItemsViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // initialize UI
        enableEdgeToEdge()
        setContent {
            FetchCodingExerciseTheme(dynamicColor = false) {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopBar(viewModel = viewModel)
                    }
                ) { innerPadding ->
                    ItemList(
                        modifier = Modifier.padding(innerPadding),
                        viewModel = viewModel)
                }
            }
        }
        // fetch data
        viewModel.fetchItems()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    viewModel: ItemsViewModel = ItemsViewModel()
) {
    val items by viewModel.items.collectAsState()
    val listId by viewModel.currentListId.collectAsState()
    var showDropDownMenu by remember { mutableStateOf(false) }
    TopAppBar(
        title = {
            if (items != null && listId != null) {
                Text("List $listId")
            }
        },
        actions = {
            if (items != null && listId != null) {
                // list selection
                IconButton(onClick = { showDropDownMenu = true }) {
                    Icon(Icons.AutoMirrored.Default.List, contentDescription = "Show more options")
                }
                DropdownMenu(
                    expanded = showDropDownMenu,
                    onDismissRequest = { showDropDownMenu = false }
                ) {
                    items!!.keys.forEach { id ->
                        val gray = MaterialTheme.colorScheme.surfaceVariant
                        val clear = Color.Transparent
                        DropdownMenuItem(
                            text = { Text(text = "List $id") },
                            onClick = {
                                viewModel.selectList(id)
                                showDropDownMenu = false
                            },
                            modifier = Modifier.background(if (id == listId) gray else clear)
                        )
                    }
                }
                // search
                Icon(Icons.Default.Search, "Search")
            }
        })
}

@Composable
fun ItemList(
    modifier: Modifier = Modifier,
    viewModel: ItemsViewModel = ItemsViewModel()
) {
    val items by viewModel.items.collectAsState()
    val listId by viewModel.currentListId.collectAsState()
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (items != null && listId != null) {
            // item list
            val itemList = items!![listId!!]!!
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(itemList) { item ->
                    ItemRow(item)
                }
            }
        } else {
            // progress bar
            CircularProgressIndicator(
                modifier = Modifier.width(64.dp))
        }
    }
}

@Composable
fun ItemRow(item: Item) {
    // single item
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Text(
            text = item.name ?: "Unnamed",
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FetchCodingExerciseTheme {
        ItemList()
    }
}
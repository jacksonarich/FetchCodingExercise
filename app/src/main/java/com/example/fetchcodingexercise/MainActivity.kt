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
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fetchcodingexercise.ui.theme.FetchCodingExerciseTheme

// This is the first screen the user sees after the app launches.
class MainActivity : ComponentActivity() {
    private val vm: ItemsViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // initialize UI
        enableEdgeToEdge()
        setContent {
            FetchCodingExerciseTheme(dynamicColor = false) {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = { TopBar(vm = vm) }
                ) { innerPadding ->
                    ItemList(
                        modifier = Modifier.padding(innerPadding),
                        vm = vm)
                }
            }
        }
        // send HTML request
        vm.fetchItems()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(vm: ItemsViewModel = ItemsViewModel()) {
    val items by vm.items.collectAsState()
    val listId by vm.currentListId.collectAsState()
    var showDropDownMenu by remember { mutableStateOf(false) }
    var showSearchField by remember { mutableStateOf(false) }
    TopAppBar(
        title = {
            if (items != null && listId != null) {
                if (showSearchField) {
                    SearchBar(vm = vm)
                } else {
                    Text("List $listId")
                }
            }
        },
        actions = {
            if (items != null && listId != null) {
                // list selection
                IconButton(onClick = { showDropDownMenu = true }) {
                    Icon(
                        Icons.AutoMirrored.Default.List,
                        contentDescription = "Select list")
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
                                vm.setListId(id)
                                showDropDownMenu = false
                            },
                            modifier = Modifier.background(if (id == listId) gray else clear)
                        )
                    }
                }
                // search
                IconButton(onClick = {
                    showSearchField = !showSearchField
                    if (!showSearchField) { vm.setSearchQuery("") }
                }) {
                    val theme = MaterialTheme.colorScheme
                    Icon(
                        Icons.Default.Search,
                        contentDescription = "Search",
                        tint = if (showSearchField) theme.background else theme.primary)
                }
            }
        })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(vm: ItemsViewModel = ItemsViewModel()) {
    val listId by vm.currentListId.collectAsState()
    val searchQuery by vm.searchQuery.collectAsState()
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    SearchBarDefaults.InputField(
        query = searchQuery,
        onQueryChange = { vm.setSearchQuery(it) },
        onSearch = { focusManager.clearFocus() },
        expanded = true,
        onExpandedChange = {},
        placeholder = { Text("Search list $listId") },
        modifier = Modifier.focusRequester(focusRequester)
    )
    LaunchedEffect(Unit) { focusRequester.requestFocus() }
}

@Composable
fun ItemList(
    modifier: Modifier = Modifier,
    vm: ItemsViewModel = ItemsViewModel()
) {
    val items by vm.items.collectAsState()
    val listId by vm.currentListId.collectAsState()
    val searchQuery by vm.searchQuery.collectAsState()
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (items != null && listId != null) {
            // item list
            var itemList = items!![listId!!]!!
            if (searchQuery.isNotBlank()) {
                itemList = itemList.filter {
                    it.unwrappedName.lowercase().contains(searchQuery)
                }
            }
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
            text = item.unwrappedName,
            modifier = Modifier.padding(16.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FetchCodingExerciseTheme {
        ItemList()
    }
}
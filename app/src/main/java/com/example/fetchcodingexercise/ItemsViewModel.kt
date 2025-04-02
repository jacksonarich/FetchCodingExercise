package com.example.fetchcodingexercise

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Locale
import java.util.SortedMap
import kotlin.math.floor

class ItemsViewModel : ViewModel() {
    private val _items = MutableStateFlow<SortedMap<Int, List<Item>>?>(null)
    val items = _items.asStateFlow()

    private val _currentListId = MutableStateFlow<Int?>(null)
    val currentListId = _currentListId.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    // retrieves dataset from the web and fills state
    fun fetchItems() {
        // launch coroutine
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("fetchItems", "Sent HTML request")
            // get the raw data, an unsorted list of all entries
            val allItems = RetrofitClient.api.getItems()
            // filter out unnamed items
            val numItems1 = allItems.count()
            Log.d("fetchItems", "Received $numItems1 items")
            val filteredItems = allItems.filter { !it.name.isNullOrEmpty() }
            val numItems2 = filteredItems.count()
            val numRemoved = numItems1 - numItems2
            val percentRemoved = formatPercent(numRemoved, numItems1)
            Log.d("fetchItems", "Filtered out $numRemoved unnamed items $percentRemoved")
            // organize into sorted lists
            val itemLists = filteredItems
                .groupBy { it.listId }
                .mapValues { (_, items) -> items.sortedBy { it.name } }
                .toSortedMap()
            // set state
            _items.value = itemLists
            _currentListId.value = itemLists.firstKey()
            // verify all items have matching names and IDs
            var numMismatch = 0
            for (item in filteredItems) {
                val actualName = item.name
                val expectedName = "Item ${item.id}"
                if (expectedName != actualName) {
                    numMismatch += 1
                }
            }
            // print result of verification
            if (numMismatch == 0) {
                Log.d("fetchItems", "Verified naming convention")
            } else {
                val percentMismatch = formatPercent(numMismatch, numItems2)
                Log.e("fetchItems", "$numMismatch items break the naming convention $percentMismatch")
            }
            // calculate histogram values
            val numCats = 10 // customizable
            val catSize = 1000.toDouble() / numCats
            val catCounts = MutableList(numCats) { 0 }
            for ((_, itemList) in itemLists) {
                for (item in itemList) {
                    val itemCat = floor(item.id.toDouble() / catSize).toInt()
                    catCounts[itemCat] += 1
                }
            }
            // print histogram values
            Log.d("fetchItems", "Histogram values: $catCounts")
        }
    }

    // calculates percentage and formats it for logging
    private fun formatPercent(numerator: Int, denominator: Int): String {
        val percent = numerator.toDouble() / denominator * 100
        val percentStr = String.format(Locale.getDefault(), "%.0f", percent)
        return "($percentStr%)"
    }

    // select a list from the dropdown
    fun setListId(id: Int) {
        _currentListId.value = id
    }

    // text changed in search field
    fun setSearchQuery(query2: String) {
        _searchQuery.value = query2.lowercase()
    }
}
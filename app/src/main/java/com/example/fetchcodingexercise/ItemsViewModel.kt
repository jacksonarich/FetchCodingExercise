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

    fun fetchItems() {
        // launch coroutine
        viewModelScope.launch(Dispatchers.IO) {
            // get the raw data, an unsorted list of all entries
            val allItems = RetrofitClient.api.getItems()
            // filter out unnamed items
            val numItems1 = allItems.count()
            Log.d("DEBUG", "Received $numItems1 items")
            val filteredItems = allItems.filter { !it.name.isNullOrEmpty() }
            val numItems2 = filteredItems.count()
            val numRemoved = numItems1 - numItems2
            val percentRemoved = getPercent(numRemoved, numItems1)
            Log.d("DEBUG", "Filtered out $numRemoved unnamed items $percentRemoved")
            // organize into sorted lists
            val itemLists = filteredItems
                .groupBy { it.listId }
                .mapValues { (_, items) -> items.sortedBy { it.name } }
                .toSortedMap()
            // set state
            _items.value = itemLists
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
                Log.d("DEBUG", "Verified naming convention")
            } else {
                val percentMismatch = getPercent(numMismatch, numItems2)
                Log.e("DEBUG", "$numMismatch items break the naming convention $percentMismatch")
            }
            // calculate histogram values
            val numClasses = 10 // customizable
            val classSize = 1000.toDouble() / numClasses
            val classNums = MutableList(numClasses) { 0 }
            for ((_, itemList) in itemLists) {
                for (item in itemList) {
                    val itemClass = floor(item.id.toDouble() / classSize).toInt()
                    classNums[itemClass] += 1
                }
            }
            // print histogram values
            Log.d("DEBUG", "Histogram values: $classNums")
        }
    }

    // calculates percentage and formats it for logging
    private fun getPercent(numerator: Int, denominator: Int): String {
        val percent = numerator.toDouble() / denominator * 100
        val percentStr = String.format(Locale.getDefault(), "%.0f", percent)
        return "($percentStr%)"
    }
}
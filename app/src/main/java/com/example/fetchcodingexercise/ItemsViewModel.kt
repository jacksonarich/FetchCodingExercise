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
//            val allItems = debugDataset
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

    private val debugDataset = mutableListOf(
        Item(755, 2, "Item 755"),
        Item(203, 2, "Item 203"),
        Item(684, 1, "Item 684"),
        Item(276, 1, "Item 276"),
        Item(736, 3, "Item 736"),
        Item(926, 4, "Item 926"),
        Item(808, 4, "Item 808"),
        Item(599, 1, "Item 599"),
        Item(424, 2, "Item 424"),
        Item(444, 1, "Item 444"),
        Item(809, 3, "Item 809"),
        Item(293, 2, "Item 293"),
        Item(510, 2, "Item 510"),
        Item(680, 3, "Item 680"),
        Item(231, 2, "Item 231"),
        Item(534, 4, "Item 534"),
        Item(294, 4, "Item 294"),
        Item(439, 1, "Item 439"),
        Item(156, 2, "Item 156"),
        Item(906, 2, "Item 906"),
        Item(49, 2, "Item 49"),
        Item(48, 2, "Item 48"),
        Item(735, 1, "Item 735"),
        Item(52, 2, "Item 52"),
        Item(681, 4, "Item 681"),
        Item(137, 3, "Item 137"),
        Item(989, 1, "Item 989"),
        Item(94, 1, "Item 94"),
        Item(177, 1, "Item 177"),
        Item(263, 1, "Item 263"),
        Item(196, 1, "Item 196"),
        Item(669, 2, "Item 669"),
        Item(710, 3, "Item 710"),
        Item(145, 1, "Item 145"),
        Item(92, 4, "Item 92"),
        Item(68, 3, "Item 68"),
        Item(364, 2, "Item 364"),
        Item(174, 2, "Item 174"),
        Item(406, 3, "Item 406"),
        Item(394, 2, "Item 394"),
        Item(624, 1, "Item 624"),
        Item(969, 4, "Item 969"),
        Item(947, 2, "Item 947"),
        Item(743, 1, "Item 743"),
        Item(419, 1, "Item 419"),
        Item(834, 4, "Item 834"),
        Item(91, 3, "Item 91"),
        Item(163, 2, "Item 163"),
        Item(471, 3, "Item 471"),
        Item(442, 1, "Item 442"),
    )
}
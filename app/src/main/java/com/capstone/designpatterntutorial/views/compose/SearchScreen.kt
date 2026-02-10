package com.capstone.designpatterntutorial.views.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.capstone.designpatterntutorial.model.mainscreen.Pattern
import com.capstone.designpatterntutorial.viewmodels.SearchMode
import com.capstone.designpatterntutorial.viewmodels.SearchState

@Composable
fun SearchScreen(
    searchState: SearchState,
    query: String,
    onQueryChange: (String) -> Unit,
    searchMode: SearchMode,
    onSearchModeChange: (SearchMode) -> Unit,
    onPatternClicked: (Pattern) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            placeholder = { Text("Search Patterns...") },
            singleLine = true
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Database")
            Switch(
                checked = searchMode == SearchMode.AI,
                onCheckedChange = {
                    onSearchModeChange(if (it) SearchMode.AI else SearchMode.DATABASE)
                }
            )
            Text("AI")
        }

        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
            when (searchState) {
                is SearchState.Loading -> {
                    CircularProgressIndicator()
                }
                is SearchState.Success -> {
                    if (searchState.patterns.isEmpty()) {
                        Text("No patterns found.")
                    } else {
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            items(searchState.patterns) {
                                PatternListItem(pattern = it, query = query, onPatternClicked = onPatternClicked)
                            }
                        }
                    }
                }
                is SearchState.Error -> {
                    Text(searchState.message)
                }
                is SearchState.Idle -> {
                    Text("Enter a pattern name to search.")
                }
            }
        }
    }
}

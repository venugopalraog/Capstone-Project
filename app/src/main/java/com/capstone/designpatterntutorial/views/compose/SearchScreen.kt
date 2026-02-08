package com.capstone.designpatterntutorial.views.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.capstone.designpatterntutorial.model.mainscreen.Pattern
import com.capstone.designpatterntutorial.viewmodels.SearchState

@Composable
fun SearchScreen(
    searchState: SearchState,
    onPatternClicked: (Pattern) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        when (searchState) {
            is SearchState.Loading -> {
                CircularProgressIndicator()
            }
            is SearchState.Success -> {
                if (searchState.patterns.isEmpty()) {
                    Text("No patterns found.")
                } else {
                    LazyColumn {
                        items(searchState.patterns) {
                            PatternListItem(pattern = it, onPatternClicked = onPatternClicked)
                        }
                    }
                }
            }
            is SearchState.Error -> {
                Text(searchState.message)
            }
            is SearchState.Idle -> {
                Text("Search for a design pattern.")
            }
        }
    }
}

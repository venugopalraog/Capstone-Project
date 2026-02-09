package com.capstone.designpatterntutorial.views.compose

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import com.capstone.designpatterntutorial.model.mainscreen.Pattern

@Composable
fun FavoriteList(patterns: List<Pattern>, onPatternClicked: (Pattern) -> Unit) {
    LazyColumn {
        items(patterns) {
            PatternListItem(pattern = it, query = "", onPatternClicked = onPatternClicked)
        }
    }
}

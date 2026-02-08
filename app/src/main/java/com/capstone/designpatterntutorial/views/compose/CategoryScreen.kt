package com.capstone.designpatterntutorial.views.compose

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import com.capstone.designpatterntutorial.model.mainscreen.Category
import com.capstone.designpatterntutorial.model.mainscreen.Pattern

@Composable
fun CategoryScreen(category: Category, onPatternClicked: (Pattern) -> Unit) {
    LazyColumn {
        items(category.patternList) {
            PatternListItem(pattern = it, onPatternClicked = onPatternClicked)
        }
    }
}
